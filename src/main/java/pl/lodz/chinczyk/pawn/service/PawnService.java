package pl.lodz.chinczyk.pawn.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.game.service.GameService;
import pl.lodz.chinczyk.pawn.model.Color;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.pawn.service.repository.PawnRepository;
import pl.lodz.chinczyk.player.model.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lodz.chinczyk.game.model.GameStatus.NEW;

@Service
public class PawnService {
    private final PawnRepository repository;
    private final GameService gameService;

    public PawnService(final PawnRepository repository,
                       final GameService gameService) {
        this.repository = repository;
        this.gameService = gameService;
    }

    public Optional<Pawn> findById(@NonNull UUID id) {
        return repository.findById(id);
    }

    public Optional<List<Pawn>> findAllByGame(@NonNull Game game) {
        return Optional.of(repository.findAllByGame(game));
    }

    public Optional<List<Pawn>> createPawnsForGame(@NonNull Player player, @NonNull UUID gameId) {
        return gameService.findById(gameId)
                .flatMap(game -> getFreeColorInGame(game)
                        .map(color -> {
                            HashSet<Pawn> pawns = new HashSet<>();
                            for (int i = 0; i < 4; i++) {
                                Pawn pawn = new Pawn();
                                pawn.setColor(color);
                                pawn.setPlayer(player);
                                pawn.setGame(game);
                                pawn.setLocation(Location.getBase(color));
                                pawns.add(pawn);
                            }
                            return repository.saveAll(pawns);
                        }));
    }

    private Optional<Color> getFreeColorInGame(@NonNull Game game) {
        return Optional.of(game)
                .filter(game1 -> game1.getStatus() == NEW)
                .map(Game::getId)
                .map(repository::findAllByGameId)
                .map(pawns -> pawns.stream()
                        .map(Pawn::getColor)
                        .collect(Collectors.toSet()))
                .flatMap(colorsInGame -> Arrays.stream(Color.values())
                        .filter(color -> !colorsInGame.contains(color))
                        .findFirst());
    }
}
