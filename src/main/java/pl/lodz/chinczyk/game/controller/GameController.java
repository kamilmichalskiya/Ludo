package pl.lodz.chinczyk.game.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.chinczyk.game.controller.mapper.GameMapper;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.game.service.DeleteService;
import pl.lodz.chinczyk.game.service.GameService;
import pl.lodz.chinczyk.pawn.service.PawnService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/games")
public class GameController {
    private final GameService service;
    private final PawnService pawnService;
    private final GameMapper mapper;
    private final DeleteService deleteService;

    @GetMapping
    @ApiOperation(value = "getAllActiveGames")
    @ApiResponse(code = 200, message = "Return list of Games with Players", response = GameDTO.class, responseContainer = "List")
    public ResponseEntity<List<GameDTO>> getAllActiveGames() {
        return service.getAllActiveGames()
                .map(mapper::mapToDTOList)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @PostMapping("/new")
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

    @PutMapping("/{gameId}/start")
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
    @ApiResponse(code = 200, message = "Roll the dice for specific player in game and get list of pawn that can be moved", response =
            UUID.class, responseContainer = "List")
    public ResponseEntity<List<UUID>> rollDice(@ApiParam(value = "Game id", required = true) @PathVariable @NonNull UUID gameId,
                                               @ApiParam(value = "Player id", required = true) @PathVariable @NonNull UUID playerId) {
        return pawnService.rollDice(gameId, playerId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "delete")
    @ApiResponse(code = 200, message = "delete")
    public void delete(){
        deleteService.delete();
    }
}
