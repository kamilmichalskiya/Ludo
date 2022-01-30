package pl.lodz.chinczyk.player.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.chinczyk.game.controller.mapper.GameMapper;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.player.service.PlayerService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService service;
    private final GameMapper mapper;

    @PutMapping("/{nick}/join/{gameId}")
    @ApiOperation(value = "joinGame")
    @ApiResponse(code = 200, message = "Return joined game", response = GameDTO.class)
    public ResponseEntity<GameDTO> joinGame(@ApiParam(value = "Player nick", required = true) @PathVariable @NonNull String nick,
                                            @ApiParam(value = "Game id", required = true) @PathVariable @NonNull UUID gameId) {
        return service.joinGame(nick, gameId)
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }
}
