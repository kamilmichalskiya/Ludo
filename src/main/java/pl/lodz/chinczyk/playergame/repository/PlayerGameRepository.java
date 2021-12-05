package pl.lodz.chinczyk.playergame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.chinczyk.playergame.model.PlayerGame;

import java.util.UUID;

@Repository
public interface PlayerGameRepository extends JpaRepository<PlayerGame, UUID> {
}
