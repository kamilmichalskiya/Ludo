package pl.lodz.chinczyk.player.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.chinczyk.pawn.service.PawnService;
import pl.lodz.chinczyk.player.model.entity.Player;
import pl.lodz.chinczyk.player.service.repository.PlayerRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {
    private final PlayerRepository repository;
    private final PawnService pawnService;

    public PlayerService(final PlayerRepository repository,
                         final PawnService pawnService) {
        this.repository = repository;
        this.pawnService = pawnService;
    }

    private Optional<Player> loadPlayer(@NonNull String nick) {
        return Optional.of(repository.findByNick(nick)
                .orElseGet(() -> repository.save(new Player(nick))));
    }

    @Transactional
    public Optional<Player> joinGame(String nick, UUID gameId) {
        return loadPlayer(nick)
                .flatMap(player -> pawnService.createPawnsForGame(player, gameId)
                        .map(pawns -> {
                            player.getPawns().addAll(pawns);
                            //TODO send information of new player in game
                            return player;
                        }));
    }
}
