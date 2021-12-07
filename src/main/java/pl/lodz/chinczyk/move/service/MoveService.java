package pl.lodz.chinczyk.move.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.move.model.entity.Move;
import pl.lodz.chinczyk.move.service.repository.MoveRepository;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.pawn.service.PawnService;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static pl.lodz.chinczyk.game.model.GameStatus.IN_PROGRESS;
import static pl.lodz.chinczyk.pawn.model.LocationType.HOME;

@Service
public class MoveService {
    private final MoveRepository repository;
    private final PawnService pawnService;

    public MoveService(final MoveRepository repository,
                       final PawnService pawnService) {
        this.repository = repository;
        this.pawnService = pawnService;
    }

    public int getRandomNumber() {
        return new Random().nextInt(5) + 1;
    }

    /*
    Plan of the "movePawn" method:
    1. Check if the pawn is in the game with the IN_PROGRESS status
    2. Check if the pawn's location is at HOME
    3. Check if the new position of the pawn is not in BASE
    4. Check if there is no pawn of the same color in the new location
    5. Send information about the pawn movement
    6. Check if any pawns will be captured
    7. If the pawn is captured, create a new pawn move from location to BASE at a distance of 0
    8. Send information about the new pawn movement to BASE
    */
    @Transactional
    public Optional<Move> movePawn(@NonNull UUID pawnId, int distance) {
        return pawnService.findById(pawnId)
                .filter(pawn -> pawn.getGame().getStatus() == IN_PROGRESS)
                .filter(pawn -> pawn.getLocation().getType() != HOME)
                .map(pawn -> createMove(distance, pawn))
                .map(this::doMove);
    }

    private Move doMove(@NonNull Move move) {
        return pawnService.getPawnInSameLocation(move)
                .map(pawn -> {
                    if (pawn.getColor() != move.getPawn().getColor()) {
                        Move save = save(move);
                        save(createMove(0, pawn));
                        return save;
                    } else {
                        return null;
                    }
                })
                .orElseGet(() -> save(move));
    }

    private Move createMove(int distance, @NonNull Pawn pawn) {
        Move move = new Move();
        move.setPawn(pawn);
        move.setOldLocation(pawn.getLocation());
        move.setNewLocation(pawn.getLocation().getLocationAfterMove(pawn.getColor(), distance));
        move.setNumber(getNumberOfMoves(pawn.getGame()) + 1);
        move.setDistance(distance);
        return move;
    }

    private Move save(@NonNull Move move) {
        move.setId(null);
        //TODO zapisz ruch i wyślij informacje do wszystkich
        return repository.save(move);
    }

    private int getNumberOfMoves(@NonNull Game game) {
        return repository.countByPawnGame(game);
    }
}
