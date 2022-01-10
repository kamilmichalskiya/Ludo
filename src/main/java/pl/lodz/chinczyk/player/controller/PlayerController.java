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
import pl.lodz.chinczyk.player.controller.mapper.PlayerMapper;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;
import pl.lodz.chinczyk.player.service.PlayerService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService service;
    private final PlayerMapper mapper;

    @PutMapping("/{nick}/join/{gameId}")
    @ApiOperation(value = "joinGame")
    @ApiResponse(code = 200, message = "Return Player", response = PlayerDTO.class)
    public ResponseEntity<PlayerDTO> joinGame(@ApiParam(value = "Player nick", required = true) @PathVariable @NonNull String nick,
                                              @ApiParam(value = "Game id", required = true) @PathVariable @NonNull UUID gameId) {
        return service.joinGame(nick, gameId)
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }
}
