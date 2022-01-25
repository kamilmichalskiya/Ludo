package pl.lodz.chinczyk.pawn.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.game.service.GameService;
import pl.lodz.chinczyk.pawn.model.Color;
import pl.lodz.chinczyk.pawn.model.Move;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.pawn.service.repository.PawnRepository;
import pl.lodz.chinczyk.player.model.entity.Player;
import pl.lodz.chinczyk.websocket.MessageSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lodz.chinczyk.game.model.GameStatus.DONE;
import static pl.lodz.chinczyk.game.model.GameStatus.IN_PROGRESS;
import static pl.lodz.chinczyk.pawn.model.Color.getNextColor;
import static pl.lodz.chinczyk.pawn.model.Location.getBase;
import static pl.lodz.chinczyk.pawn.model.Location.getHome;
import static pl.lodz.chinczyk.pawn.model.Location.getStartLocation;
import static pl.lodz.chinczyk.pawn.model.LocationType.BASE;
import static pl.lodz.chinczyk.pawn.model.LocationType.HOME;

@Service
@RequiredArgsConstructor
public class PawnService {
    private final PawnRepository repository;
    private final GameService gameService;
    private final MessageSender messageSender;

    public List<Pawn> createPawnsForGame(@NonNull Player player, @NonNull UUID gameId) {
        return gameService.findById(gameId)
                .flatMap(game -> getFreeColorInGame(game)
                        .map(color -> {
                            HashSet<Pawn> pawns = new HashSet<>();
                            for (int i = 0; i < 4; i++) {
                                Pawn pawn = new Pawn();
                                pawn.setColor(color);
                                pawn.setPlayer(player);
                                pawn.setGame(game);
                                pawn.setLocation(getBase(color));
                                pawns.add(pawn);
                            }
                            return repository.saveAll(pawns);
                        }))
                .orElseGet(ArrayList::new);
    }

    private Optional<Color> getFreeColorInGame(@NonNull Game game) {
        return Optional.of(game)
                .map(repository::findAllByGame)
                .map(pawnsInGame -> pawnsInGame.stream()
                        .map(Pawn::getColor)
                        .collect(Collectors.toSet()))
                .flatMap(colorsInGame -> Arrays.stream(Color.values())
                        .filter(color -> !colorsInGame.contains(color))
                        .findFirst());
    }

    public Optional<Pawn> movePawn(@NonNull UUID pawnId, @NonNull Integer distance) {
        return getSelectedPawn(pawnId, distance)
                .map(selectedPawn -> {
                    List<Pawn> possibleMoves = getPossibleMoves(selectedPawn.getGame().getId(), selectedPawn.getColor(), distance);
                    if (possibleMoves.isEmpty()) {
                        prepareGameForNextTurn(selectedPawn.getGame().getId(), distance, selectedPawn.getColor());
                        messageSender.updateGame(selectedPawn.getGame());
                        return null;
                    } else if (possibleMoves.stream().noneMatch(pawn -> pawn.getId().equals(selectedPawn.getId()))) {
                        return null;
                    } else {
                        Move move = createMove(selectedPawn, distance);
                        takePawn(move);
                        prepareGameForNextTurn(selectedPawn.getGame().getId(), distance, selectedPawn.getColor());
                        selectedPawn.setLocation(move.getNewLocation());
                        messageSender.updateGame(selectedPawn.getGame());
                        return repository.save(selectedPawn);
                    }
                });
    }

    public List<Pawn> getPossibleMoves(UUID gameId, Color color, Integer distance) {
        return repository.findAllByGameId(gameId).stream()
                .filter(pawn -> pawn.getColor().equals(color))
                .filter(pawn -> !pawn.getLocation().getType().equals(HOME))
                .filter(pawn -> !(pawn.getLocation().getType().equals(BASE) && distance != 6))
                .collect(Collectors.toList());
    }

    private Optional<Pawn> getSelectedPawn(UUID pawnId, Integer distance) {
        return repository.findById(pawnId)
                .filter(pawn -> pawn.getGame().getStatus().equals(IN_PROGRESS))
                .filter(pawn -> pawn.getGame().getNextPlayerId().equals(pawn.getPlayer().getId()))
                .filter(pawn -> distance >= 1 && distance <= 6);
    }

    @Transactional
    public void prepareGameForNextTurn(@NonNull UUID gameId, int distance, @NonNull Color currentColor) {
        gameService.findById(gameId)
                .ifPresent(game -> {
                    if (isGameDone(game.getPawns(), currentColor)) {
                        game.setStatus(DONE);
                    } else if (distance != 6) {
                        Map<Color, UUID> mapOfPlayersAndColors = game.getPlayers().stream()
                                .collect(Collectors.toMap(player -> player.getPawns().stream().findAny().map(Pawn::getColor).orElse(null), Player::getId));
                        Color nextColor = getNextColor(currentColor);
                        while (!mapOfPlayersAndColors.containsKey(nextColor)) {
                            nextColor = getNextColor(nextColor);
                        }
                        game.setNextPlayerId(mapOfPlayersAndColors.get(nextColor));
                    }
                });
    }

    private boolean isGameDone(@NonNull Set<Pawn> pawns, @NonNull Color currentColor) {
        return pawns.stream()
                .filter(pawn -> pawn.getColor() == currentColor)
                .allMatch(pawn -> pawn.getLocation() == getHome(currentColor));
    }

    private Move createMove(@NonNull Pawn pawn, int distance) {
        Move move = new Move();
        move.setPawn(pawn);
        move.setOldLocation(pawn.getLocation());
        move.setNewLocation(pawn.getLocation().getLocationAfterMove(pawn.getColor(), distance));
        move.setDistance(distance);
        return move;
    }

    private void takePawn(@NonNull Move move) {
        getPawnsInSameLocation(move)
                .ifPresent(pawnList -> pawnList.stream()
                        .filter(pawn -> pawn.getLocation() != getStartLocation(pawn.getColor()))
                        .filter(pawn -> pawn.getColor() != move.getPawn().getColor())
                        .forEach(pawn -> {
                            pawn.setLocation(getBase(pawn.getColor()));
                            repository.save(pawn);
                        }));
    }

    private Optional<List<Pawn>> getPawnsInSameLocation(@NonNull Move move) {
        return Optional.of(move.getPawn())
                .map(Pawn::getGame)
                .map(repository::findAllByGame)
                .map(pawns -> pawns.stream()
                        .filter(pawn -> pawn.getLocation().getType() != BASE)
                        .filter(pawn -> pawn.getLocation().getType() != HOME)
                        .filter(pawn -> pawn.getLocation() == move.getNewLocation())
                        .collect(Collectors.toList()));
    }

    public Optional<List<UUID>> rollDice(@NonNull UUID gameId, @NonNull UUID playerId) {
        final Color[] color = new Color[1];
        return gameService.findById(gameId)
                .map(game -> {
                    color[0] = game.getPawns().stream().filter(pawn -> pawn.getPlayer().getId().equals(playerId)).findFirst().map(Pawn::getColor).orElse(null);
                    return game.getNextPlayerId();
                })
                .filter(playerId::equals)
                .map(nextPlayerId -> new Random().nextInt(6) + 1)
                .map(distance -> {
                    messageSender.sendRollDiceInfo(gameId, distance);
                    return getPossibleMoves(gameId, color[0], distance).stream().map(Pawn::getId).collect(Collectors.toList());
                });
    }
}
