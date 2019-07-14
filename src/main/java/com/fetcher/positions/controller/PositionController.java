package com.fetcher.positions.controller;

import com.fetcher.positions.entity.PositionView;
import com.fetcher.positions.entity.request.ImportRequest;
import com.fetcher.positions.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("api/v1/positions")
public class PositionController {

    private PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/import")
    ResponseEntity importPositions(@RequestBody ImportRequest request){
        positionService.importAll(request.getUrl(), request.getCount());
        return new ResponseEntity("Success", HttpStatus.CREATED);
    }

    @GetMapping("/findBy")
    ResponseEntity<List<PositionView>> findBy(@RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "location", required = false) String location,
                                              @RequestParam(value = "description", required = false) String description){
        List<PositionView> result = positionService.findPositionBy(type, location, description);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
