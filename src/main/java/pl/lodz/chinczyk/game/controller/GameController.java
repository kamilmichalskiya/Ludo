package pl.lodz.chinczyk.game.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.chinczyk.game.controller.mapper.GameMapper;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.game.service.GameService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {
    private final GameService service;
    private final GameMapper mapper;

    @GetMapping
    @ApiOperation(value = "getAllActiveGames")
    @ApiResponse(code = 200, message = "Return list of Games with Players", response = GameDTO.class, responseContainer = "List")
    public ResponseEntity<List<GameDTO>> getAllActiveGames() {
        return service.getAllActiveGames()
                .map(mapper::mapToDTOList)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/new")
    @ApiOperation(value = "createGame")
    @ApiResponse(code = 200, message = "Return new Game", response = GameDTO.class)
    public ResponseEntity<GameDTO> createGame() {
        return service.createGame()
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/{gameId}")
    @ApiOperation(value = "getGame")
    @ApiResponse(code = 200, message = "Return game information", response = GameDTO.class)
    public ResponseEntity<GameDTO> getGame(@ApiParam(value = "Game id", required = true) @PathVariable @NonNull UUID gameId) {
        return service.findById(gameId)
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/{gameId}/start")
    @ApiOperation(value = "startGame")
    @ApiResponse(code = 200, message = "Start game and return game information", response = GameDTO.class)
    public ResponseEntity<GameDTO> startGame(@ApiParam(value = "Game id", required = true) @PathVariable @NonNull UUID gameId) {
        return service.startGame(gameId)
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/{gameId}/player/{playerId}/dice")
    @ApiOperation(value = "rollDice")
    @ApiResponse(code = 200, message = "Roll the dice for specific player in game and get value", response = GameDTO.class)
    public ResponseEntity<Integer> rollDice(@ApiParam(value = "Game id", required = true) @PathVariable @NonNull UUID gameId,
                                            @ApiParam(value = "Player id", required = true) @PathVariable @NonNull UUID playerId) {
        return ResponseEntity.ok(service.rollDice(gameId, playerId));
    }
}
