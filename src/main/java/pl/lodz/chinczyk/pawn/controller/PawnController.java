package pl.lodz.chinczyk.pawn.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.chinczyk.pawn.controller.mapper.PawnMapper;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.pawn.service.PawnService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/pawns")
public class PawnController {
    private final PawnService service;
    private final PawnMapper mapper;

    @PutMapping("/{pawnId}/move/{distance}")
    @ApiOperation(value = "movePawn")
    @ApiResponse(code = 200, message = "Move pawn in game", response = PawnDTO.class)
    public ResponseEntity<PawnDTO> movePawn(@PathVariable @NonNull UUID pawnId, @PathVariable @NonNull int distance) {
        return service.movePawn(pawnId, distance)
                .map(mapper::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.badRequest()::build);
    }
}
