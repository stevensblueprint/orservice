package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.service.ProgramService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
  public final ProgramService programService;

  @Autowired
  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

  @GetMapping
  public ResponseEntity<PaginationDTO<ProgramDTO>> getAllPrograms(@RequestParam(defaultValue = "1") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer perPage) {
    List<ProgramDTO> programDTOs = this.programService.getAllPrograms();

    try {
      PaginationDTO<ProgramDTO> paginationDTO = PaginationDTO.of(
              programDTOs,
              page,
              perPage
      );
      return ResponseEntity.ok(paginationDTO);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProgramDTO> getProgramById(@PathVariable String id) {
    ProgramDTO programDTO = this.programService.getProgramDTOById(id);
    return ResponseEntity.ok(programDTO);
  }

  @PostMapping
  public ResponseEntity<ProgramDTO> createProgram(@RequestBody ProgramDTO programDTO) {
    ProgramDTO createdProgramDTO = this.programService.createProgram(programDTO);
    return ResponseEntity.ok(createdProgramDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProgramDTO> updateProgram(@PathVariable String id, @RequestBody ProgramDTO programDTO) {
    ProgramDTO updatedProgramDTO = this.programService.updateProgram(id, programDTO);
    return ResponseEntity.ok(updatedProgramDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProgram(@PathVariable String id) {
    this.programService.deleteProgram(id);
    return ResponseEntity.noContent().build();
  }
}
