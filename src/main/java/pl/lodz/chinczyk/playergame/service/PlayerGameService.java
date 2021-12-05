package pl.lodz.chinczyk.playergame.service;

import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.playergame.repository.PlayerGameRepository;

@Service
public class PlayerGameService {
    private final PlayerGameRepository repository;

    public PlayerGameService(final PlayerGameRepository repository) {
        this.repository = repository;
    }
}
