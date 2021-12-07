package pl.lodz.chinczyk.game.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.game.service.repository.GameRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.lodz.chinczyk.game.model.GameStatus.IN_PROGRESS;
import static pl.lodz.chinczyk.game.model.GameStatus.NEW;

@Service
public class GameService {

    private final GameRepository repository;

    public GameService(final GameRepository repository) {
        this.repository = repository;
    }

    public Optional<Game> createGame() {
        Game game = new Game();
        game.setStatus(NEW);
        //TODO send information of new game (websocket)
        return Optional.of(repository.save(game));
    }

    public Optional<List<Game>> getAllNewGames() {
        return Optional.of(repository.findAllByStatus(NEW));
    }

    public Optional<Game> startGame(@NonNull UUID id) {
        return findById(id)
                .filter(game -> game.getStatus() == NEW && game.getPawns().size() >= 8)//8 - minimum pawns (2 players)
                .map(game -> {
                    game.setStatus(IN_PROGRESS);
                    //TODO send information of not available to join game (websocket)
                    return game;
                });
    }

    public Optional<Game> findById(@NonNull UUID id) {
        return repository.findById(id);
    }
}
