package pl.lodz.chinczyk.player.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.chinczyk.game.service.GameService;
import pl.lodz.chinczyk.pawn.model.entity.Pawn;
import pl.lodz.chinczyk.pawn.service.PawnService;
import pl.lodz.chinczyk.player.model.entity.Player;
import pl.lodz.chinczyk.player.service.repository.PlayerRepository;
import pl.lodz.chinczyk.websocket.MessageSender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.lodz.chinczyk.game.model.GameStatus.IN_PROGRESS;
import static pl.lodz.chinczyk.game.model.GameStatus.NEW;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;
    private final GameService gameService;
    private final PawnService pawnService;
    private final MessageSender messageSender;

    @Transactional
    public Optional<Player> joinGame(@NonNull String nick, @NonNull UUID gameId) {
        return gameService.findById(gameId)
                .flatMap(game -> {
                    if (game.getStatus() == NEW || game.getStatus() == IN_PROGRESS) {
                        if (game.getPlayers().stream().map(Player::getNick).anyMatch(nick::equals)) {
                            return repository.findByNick(nick);
                        } else if (game.getStatus() == NEW && game.getPlayers().size() < 4) {
                            return Optional.of(repository.findByNick(nick)
                                    .orElseGet(() -> repository.save(new Player(nick))))
                                    .map(player -> {
                                        player.getGames().add(game);
                                        List<Pawn> pawnList = pawnService.createPawnsForGame(player, gameId);
                                        player.getPawns().addAll(pawnList);
                                        game.getPawns().addAll(pawnList);
                                        game.getPlayers().add(player);
                                        messageSender.updateListOfGames(game);
                                        messageSender.updateGame(game);
                                        return repository.save(player);
                                    });
                        }
                    }
                    return Optional.empty();
                });
    }
}
