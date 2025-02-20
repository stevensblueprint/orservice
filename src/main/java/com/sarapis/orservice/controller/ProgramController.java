package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.exception.InvalidInputException;
import com.sarapis.orservice.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
    public final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

  @GetMapping
  public ResponseEntity<PaginationDTO<ProgramDTO>> getAllPrograms(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                  @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
    List<ProgramDTO> programDTOs = this.programService.getAllPrograms();

    if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
    if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");

    PaginationDTO<ProgramDTO> paginationDTO = PaginationDTO.of(
        programDTOs,
        page,
        perPage
    );
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProgramDTO> getProgramById(@PathVariable String id) {
    ProgramDTO programDTO = programService.getProgramById(id);
    return ResponseEntity.ok(programDTO);
  }

  @PostMapping
  public ResponseEntity<ProgramDTO> createProgram(@RequestBody ProgramDTO programDTO) {
    ProgramDTO createdProgram = programService.createProgram(programDTO);
    return ResponseEntity.ok(createdProgram);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProgramDTO> updateProgram(@PathVariable String id, @RequestBody ProgramDTO programDTO) {
    ProgramDTO updatedProgram = programService.updateProgram(id, programDTO);
    return ResponseEntity.ok(updatedProgram);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProgram(@PathVariable String id) {
    programService.deleteProgram(id);
    return ResponseEntity.noContent().build();
  }
}
