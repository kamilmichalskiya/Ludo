package pl.lodz.chinczyk.pawn.service;

import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.pawn.repository.PawnRepository;

@Service
public class PawnService {
    private final PawnRepository repository;

    public PawnService(final PawnRepository repository) {
        this.repository = repository;
    }
}
