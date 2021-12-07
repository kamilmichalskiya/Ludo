package pl.lodz.chinczyk.pawn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.chinczyk.pawn.controller.mapper.PawnMapper;
import pl.lodz.chinczyk.pawn.service.PawnService;

@RestController
@RequestMapping(path = "/v1/moves")
public class PawnController {

    private final PawnService service;
    private final PawnMapper mapper;

    public PawnController(final PawnService service, final PawnMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }
}
