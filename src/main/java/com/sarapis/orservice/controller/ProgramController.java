package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.service.ProgramService;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/programs")
public class ProgramController {
  public final ProgramService programService;

  @Autowired
  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

  @GetMapping
  public ResponseEntity<List<ProgramDTO>> getAllPrograms() {
    List<ProgramDTO> programDTOs = this.programService.getAllPrograms();
    return ResponseEntity.ok(programDTOs);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProgramDTO> getProgramById(@PathVariable String id) {
    ProgramDTO programDTO = this.programService.getProgramDTOById(id);
    return ResponseEntity.ok(programDTO);
  }

  @PostMapping
  public ResponseEntity<ProgramDTO> createProgram(@RequestBody ProgramDTO programDTO) {
    if (programDTO.getId() == null) {
      programDTO.setId(UUID.randomUUID().toString());
    }
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
