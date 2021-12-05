package pl.lodz.chinczyk.game.service;

import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.game.repository.GameRepository;

@Service
public class GameService {

    private final GameRepository repository;

    public GameService(final GameRepository repository) {
        this.repository = repository;
    }
}
