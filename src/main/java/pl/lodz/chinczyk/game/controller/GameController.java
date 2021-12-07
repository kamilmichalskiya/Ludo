package pl.lodz.chinczyk.game.controller;

import lombok.NonNull;
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
@RequestMapping("/games")
public class GameController {
    private final GameService service;
    private final GameMapper mapper;

    public GameController(final GameService service, final GameMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllNewGames() {
        return service.getAllNewGames()
                .map(mapper::mapToDTOList)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/new")
    public ResponseEntity<GameDTO> createGame() {
        return service.createGame()
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }

    @GetMapping("/{id}/start")
    public ResponseEntity<GameDTO> startGame(@PathVariable @NonNull UUID id) {
        return service.startGame(id)
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

}
