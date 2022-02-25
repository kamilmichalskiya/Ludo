package pl.lodz.chinczyk.websocket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.lodz.chinczyk.game.controller.mapper.GameMapper;
import pl.lodz.chinczyk.game.model.entity.Game;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageSender {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameMapper gameMapper;

    public Game updateListOfGames(@NonNull Game game) {
        messagingTemplate.convertAndSend("/game/all", gameMapper.mapToDTO(game));
        return game;
    }

    public void updateGame(@NonNull Game game) {
        messagingTemplate.convertAndSend("/game/" + game.getId(), gameMapper.mapToDTO(game));
    }

    public void sendRollDiceInfo(UUID gameId, Integer distance) {
        messagingTemplate.convertAndSend("/roll/" + gameId, distance);
    }
}
