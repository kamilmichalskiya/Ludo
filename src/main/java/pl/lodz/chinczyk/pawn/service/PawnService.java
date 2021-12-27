package pl.lodz.chinczyk.pawn.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.game.service.GameService;
import pl.lodz.chinczyk.pawn.model.Color;
import pl.lodz.chinczyk.pawn.model.Location;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lodz.chinczyk.game.model.GameStatus.DONE;
import static pl.lodz.chinczyk.game.model.GameStatus.IN_PROGRESS;
import static pl.lodz.chinczyk.pawn.model.Color.NO_COLOR;
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

    @Transactional
    public Optional<Pawn> movePawn(@NonNull UUID pawnId, @NonNull Integer distance) {
        return repository.findById(pawnId)
                .filter(pawn -> pawn.getGame().getStatus() == IN_PROGRESS)
                .filter(pawn -> pawn.getGame().getNextPlayerId() == pawn.getPlayer().getId())
                .filter(pawn -> pawn.getLocation().getType() != HOME)
                .filter(pawn -> !(pawn.getLocation().getType() == BASE && distance != 6))
                .map(pawn -> {
                    Move move = createMove(pawn, distance);
                    pawn.setLocation(move.getNewLocation());
                    doMove(move);
                    prepareGameForNextTurn(pawn.getGame().getId(), distance, pawn.getColor());
                    return pawn;
                });
    }

    private void prepareGameForNextTurn(@NonNull UUID gameId, int distance, @NonNull Color currentColor) {
        gameService.findById(gameId)
                .ifPresent(game -> {
                    if (isGameDone(game.getPawns(), currentColor)) {
                        game.setStatus(DONE);
                    } else if (distance != 6) {
                        Map<Color, UUID> mapOfPlayersAndColors = game.getPlayers().stream()
                                .collect(Collectors
                                        .toMap(player -> player.getPawns().stream().findAny().map(Pawn::getColor).orElse(NO_COLOR),
                                                Player::getId));
                        Color nextColor;
                        do {
                            nextColor = getNextColor(currentColor);
                        } while (!mapOfPlayersAndColors.containsKey(nextColor));
                        game.setNextPlayerId(mapOfPlayersAndColors.get(nextColor));
                    }
                    messageSender.updateGame(game);
                });
    }

    private boolean isGameDone(@NonNull Set<Pawn> pawns, @NonNull Color currentColor) {
        return pawns.stream()
                .filter(pawn -> pawn.getColor() == currentColor)
                .allMatch(pawn -> pawn.getLocation() == getHome(pawn.getColor()));
    }

    private Move createMove(@NonNull Pawn pawn, int distance) {
        Move move = new Move();
        move.setPawn(pawn);
        move.setOldLocation(pawn.getLocation());
        move.setNewLocation(pawn.getLocation().getLocationAfterMove(pawn.getColor(), distance));
        move.setDistance(distance);
        return move;
    }

    private void doMove(@NonNull Move move) {
        getPawnsInSameLocation(move)
                .ifPresent(pawnList -> pawnList.stream()
                        .filter(pawn -> pawn.getLocation() != getStartLocation(pawn.getColor()))
                        .filter(pawn -> pawn.getColor() != move.getPawn().getColor())
                        .forEach(pawn -> pawn.setLocation(Location.getBase(pawn.getColor()))));
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
}
