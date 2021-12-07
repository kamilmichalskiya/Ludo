package pl.lodz.chinczyk.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.chinczyk.game.model.GameStatus;
import pl.lodz.chinczyk.game.model.entity.Game;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findAllByStatus(GameStatus status);
}
