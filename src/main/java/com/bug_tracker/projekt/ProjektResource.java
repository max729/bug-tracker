package com.bug_tracker.projekt;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/projekts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjektResource {

    private final ProjektService projektService;

    public ProjektResource(final ProjektService projektService) {
        this.projektService = projektService;
    }

    @GetMapping
    public ResponseEntity<List<ProjektDTO>> getAllProjekts() {
        return ResponseEntity.ok(projektService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjektDTO> getProjekt(@PathVariable final Long id) {
        return ResponseEntity.ok(projektService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createProjekt(@RequestBody @Valid final ProjektDTO projektDTO) {
        return new ResponseEntity<>(projektService.create(projektDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProjekt(@PathVariable final Long id,
            @RequestBody @Valid final ProjektDTO projektDTO) {
        projektService.update(id, projektDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProjekt(@PathVariable final Long id) {
        projektService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
