package pl.lodz.chinczyk.pawn.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;

import java.util.List;
import java.util.UUID;

@Repository //TODO wyrzucenie adnotacji
public interface PawnRepository extends JpaRepository<Pawn, UUID> {
    List<Pawn> findAllByGame(Game game);

    List<Pawn> findAllByGameId(UUID gameId);
}
