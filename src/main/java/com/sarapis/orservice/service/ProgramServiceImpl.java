package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ProgramRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProgramServiceImpl implements ProgramService {
  private final ProgramRepository programRepository;
  private final AttributeRepository attributeRepository;
  private final MetadataRepository metadataRepository;

  public ProgramServiceImpl(ProgramRepository programRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
    this.programRepository = programRepository;
    this.attributeRepository = attributeRepository;
    this.metadataRepository = metadataRepository;
  }

  @Override
  public List<ProgramDTO> getAllPrograms() {
    List<ProgramDTO> programDTOs = this.programRepository.findAll().stream().map(Program::toDTO).toList();
    programDTOs.forEach(this::addRelatedData);
    return programDTOs;
  }

  @Override
  public ProgramDTO getProgramDTOById(String id) {
    Program program = this.programRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Program not found.")
    );
    ProgramDTO programDTO = program.toDTO();
    this.addRelatedData(programDTO);
    return programDTO;
  }

  @Override
  public ProgramDTO createProgram(ProgramDTO programDTO) {
    Program program = this.programRepository.save(programDTO.toEntity());
    for (AttributeDTO attributeDTO : programDTO.getAttributes()) {
      this.attributeRepository.save(attributeDTO.toEntity(program.getId()));
    }

    for (MetadataDTO metadataDTO : programDTO.getMetadata()) {
      this.metadataRepository.save(metadataDTO.toEntity(program.getId()));
    }

    ProgramDTO savedProgramDTO = this.programRepository.save(program).toDTO();
    this.addRelatedData(savedProgramDTO);
    return savedProgramDTO;
  }

  @Override
  public ProgramDTO updateProgram(String id, ProgramDTO programDTO) {
    Program oldProgram = this.programRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Program not found."));
    oldProgram.setId(programDTO.getId());
    oldProgram.setName(programDTO.getName());
    oldProgram.setAlternateName(programDTO.getAlternateName());
    oldProgram.setDescription(programDTO.getDescription());

    Program updatedProgram = this.programRepository.save(oldProgram);
    return updatedProgram.toDTO();
  }

  @Override
  public void deleteProgram(String id) {
    Program program = this.programRepository.findById(id).orElseThrow(() -> new RuntimeException("Program not found."));
    this.programRepository.deleteAttributes(program.getId());
    this.programRepository.deleteMetadata(program.getId());
    this.programRepository.delete(program);
  }

  private void addRelatedData(ProgramDTO programDTO) {
    programDTO.getAttributes().addAll(this.programRepository.getAttributes(programDTO.getId()).stream().map(
        Attribute::toDTO).toList());
    programDTO.getMetadata().addAll(this.programRepository.getMetadata(programDTO.getId()).stream().map(
        Metadata::toDTO).toList());
  }
}
