package pl.lodz.chinczyk.move.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.chinczyk.move.model.Move;

import java.util.UUID;

@Repository
public interface MoveRepository extends JpaRepository<Move, UUID> {
}
