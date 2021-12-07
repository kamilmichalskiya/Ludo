package pl.lodz.chinczyk.move.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.chinczyk.game.model.entity.Game;
import pl.lodz.chinczyk.move.model.entity.Move;

import java.util.UUID;

@Repository
public interface MoveRepository extends JpaRepository<Move, UUID> {
    int countByPawnGame(Game game);
}
