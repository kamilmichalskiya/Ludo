package pl.lodz.chinczyk.pawn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.chinczyk.pawn.model.Pawn;

import java.util.UUID;

@Repository
public interface PawnRepository extends JpaRepository<Pawn, UUID> {
}
