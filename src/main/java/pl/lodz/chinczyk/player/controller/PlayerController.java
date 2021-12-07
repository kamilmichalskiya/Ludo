package pl.lodz.chinczyk.player.controller;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.chinczyk.player.controller.mapper.PlayerMapper;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;
import pl.lodz.chinczyk.player.service.PlayerService;

import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService service;
    private final PlayerMapper mapper;

    public PlayerController(final PlayerService service,
                            final PlayerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/{nick}/join/{id}")
    public ResponseEntity<PlayerDTO> joinGame(@PathVariable @NonNull String nick, @PathVariable @NonNull UUID id) {
        return service.joinGame(nick, id)
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }
}
