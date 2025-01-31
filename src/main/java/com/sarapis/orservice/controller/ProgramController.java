package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.upsert.UpsertLocationDTO;
import com.sarapis.orservice.dto.upsert.UpsertProgramDTO;
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

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramDTO> getProgramById(@PathVariable String programId) {
        ProgramDTO program = this.programService.getProgramDTOById(programId);
        return ResponseEntity.ok(program);
    }

    @PostMapping
    public ResponseEntity<ProgramDTO> createProgram(@RequestBody UpsertProgramDTO upsertProgramDTO) {
        ProgramDTO createdProgram = this.programService.createProgram(upsertProgramDTO);
        return ResponseEntity.ok(createdProgram);
    }

    @PutMapping("/{programId}")
    public ResponseEntity<ProgramDTO> updateProgram(@PathVariable String programId,
                                                    @RequestBody ProgramDTO programDTO) {
        ProgramDTO updatedProgram = this.programService.updateProgram(programId, programDTO);
        return ResponseEntity.ok(updatedProgram);
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> deleteProgram(@PathVariable String programId) {
        this.programService.deleteProgram(programId);
        return ResponseEntity.noContent().build();
    }
}
