package pl.lodz.chinczyk.move.service;

import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.move.repository.MoveRepository;

@Service
public class MoveService {
    private final MoveRepository repository;

    public MoveService(final MoveRepository repository) {
        this.repository = repository;
    }
}
