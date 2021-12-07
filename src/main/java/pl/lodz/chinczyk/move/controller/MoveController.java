package pl.lodz.chinczyk.move.controller;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.chinczyk.move.controller.mapper.MoveMapper;
import pl.lodz.chinczyk.move.model.dto.MoveDTO;
import pl.lodz.chinczyk.move.service.MoveService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/moves")
public class MoveController {

    private final MoveService service;
    private final MoveMapper mapper;

    public MoveController(final MoveService service,
                          final MoveMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/dice")
    public ResponseEntity<Integer> getRandomNumber() {
        return ResponseEntity.ok(service.getRandomNumber());
    }

    @GetMapping("/{pawnId}")
    public ResponseEntity<MoveDTO> movePawn(@PathVariable @NonNull UUID pawnId, @RequestBody @NonNull MoveDTO dto) {
        return service.movePawn(pawnId, dto.getDistance())
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

}
