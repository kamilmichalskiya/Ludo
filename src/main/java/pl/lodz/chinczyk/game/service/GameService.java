package pl.lodz.chinczyk.game.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.game.service.repository.GameRepository;
import pl.lodz.chinczyk.player.model.entity.Player;
import pl.lodz.chinczyk.websocket.MessageSender;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lodz.chinczyk.game.model.GameStatus.IN_PROGRESS;
import static pl.lodz.chinczyk.game.model.GameStatus.NEW;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository repository;
    private final MessageSender messageSender;

    public Optional<List<Game>> getAllActiveGames() {
        return Optional.of(repository.findAll()
                .stream()
                .filter(game -> game.getStatus() == NEW || game.getStatus() == IN_PROGRESS)
                .collect(Collectors.toList()));
    }

    public Optional<Game> createGame() {
        Game game = new Game();
        game.setStatus(NEW);
        return Optional.of(repository.save(game))
                .map(messageSender::updateListOfGames);
    }

    public Optional<Game> findById(@NonNull UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public Optional<Game> startGame(@NonNull UUID id) {
        return findById(id)
                .filter(game -> game.getStatus() == NEW && game.getPlayers().size() >= 2)
                .map(game -> {
                    game.setStatus(IN_PROGRESS);
                    game.getPlayers()
                            .stream()
                            .findFirst()
                            .map(Player::getId)
                            .ifPresent(game::setNextPlayerId);
                    messageSender.updateGame(game);
                    messageSender.updateListOfGames(game);
                    return game;
                });
    }

    public int rollDice(@NonNull UUID gameId, @NonNull UUID playerId) {
        return repository.findById(gameId)
                .map(Game::getPlayers)
                .filter(players -> players.stream().anyMatch(player -> player.getId() == playerId))
                .map(players -> new Random().nextInt(5) + 1)
                .orElse(0);
    }
}
