package pl.lodz.chinczyk.player.service;

import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.player.repository.PlayerRepository;

@Service
public class PlayerService {
    private final PlayerRepository repository;

    public PlayerService(final PlayerRepository repository) {
        this.repository = repository;
    }
}
