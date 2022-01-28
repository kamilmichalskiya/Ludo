package pl.lodz.chinczyk.game.service;

import org.springframework.stereotype.Service;
import pl.lodz.chinczyk.game.service.repository.GameRepository;
import pl.lodz.chinczyk.pawn.service.repository.PawnRepository;
import pl.lodz.chinczyk.player.service.repository.PlayerRepository;

@Service
public class DeleteService {

    PlayerRepository playerRepository;
    GameRepository gameRepository;
    PawnRepository pawnRepository;

    public void delete() {
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        pawnRepository.deleteAll();
    }
}
