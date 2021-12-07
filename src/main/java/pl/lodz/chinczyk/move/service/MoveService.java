package pl.lodz.chinczyk.move.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.move.model.entity.Move;
import pl.lodz.chinczyk.move.service.repository.MoveRepository;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.pawn.service.PawnService;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static pl.lodz.chinczyk.game.model.GameStatus.IN_PROGRESS;
import static pl.lodz.chinczyk.pawn.model.LocationType.BASE;
import static pl.lodz.chinczyk.pawn.model.LocationType.HOME;

@Service
public class MoveService {
    private final MoveRepository repository;
    private final PawnService pawnService;

    public MoveService(final MoveRepository repository, final PawnService pawnService) {
        this.repository = repository;
        this.pawnService = pawnService;
    }

    public Optional<Move> save(@NonNull Move move) {
        move.setId(null);
        return Optional.of(repository.save(move));
    }

    public int getNumber(Game game) {
        return repository.countByPawnGame(game);
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
                .map(pawn -> {
                    Move move = new Move();
                    move.setOldLocation(pawn.getLocation());
                    move.setNewLocation(pawn.getLocation().getNewLocation(pawn.getColor(), distance));
                    move.setDistance(distance);
                    move.setPawn(pawn);
                    return move;
                })
                .flatMap(move -> Optional.of(move.getPawn())
                        .map(Pawn::getGame)
                        .flatMap(pawnService::findAllByGame)
                        .flatMap(pawns -> {
                            Stream<Pawn> pawnStream = pawns.stream()
                                    .filter(pawn -> pawn.getLocation().getType() != BASE)
                                    .filter(pawn -> pawn.getLocation().getType() != HOME);
                            if (pawnStream.noneMatch(pawn -> pawn.getLocation() == move.getNewLocation())) {
                                //TODO zapisz ruch i wyślij informacje do wszystkich
                                //TODO dodaj nr ruchu
                                return save(move);
                            } else {
                                return pawnStream.filter(pawn -> pawn.getColor() != move.getPawn().getColor())
                                        .findFirst()
                                        .flatMap(pawn -> {
                                            move.setNumber(getNumber(pawn.getGame()));
                                            //TODO zapisz ruch i wyślij informacje do wszystkich
                                            //TODO dodaj nr ruchu
                                            return save(move)
                                                    .map(firstMove -> {
                                                        Move secondMove = new Move();
                                                        secondMove.setPawn(pawn);
                                                        secondMove.setDistance(0);
                                                        secondMove.setOldLocation(pawn.getLocation());
                                                        secondMove.setNewLocation(Location.getBase(pawn.getColor()));
                                                        secondMove.setNumber(firstMove.getNumber() + 1);
                                                        save(secondMove);
                                                        //TODO stwórz ruch zbijający i zapisz i roześlij
                                                        return firstMove;
                                                    });

                                        });
                            }
                        }));
    }
}
