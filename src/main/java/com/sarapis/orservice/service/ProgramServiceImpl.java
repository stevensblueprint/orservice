package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public List<ProgramDTO> getAllPrograms() {
        return programRepository.findAll().stream()
            .map(Program::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ProgramDTO getProgramById(String id) {
        Program program = programRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));
        return program.toDTO();
    }

    @Override
    public ProgramDTO createProgram(ProgramDTO programDTO) {
        // Generate an ID if not provided
        if (programDTO.getId() == null || programDTO.getId().isEmpty()) {
            programDTO.setId(UUID.randomUUID().toString());
        }
        Program program = mapToEntity(programDTO);
        Program savedProgram = programRepository.save(program);
        return savedProgram.toDTO();
    }

    @Override
    public ProgramDTO updateProgram(String id, ProgramDTO programDTO) {
        Program existingProgram = programRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));
        existingProgram.setName(programDTO.getName());
        existingProgram.setAlternateName(programDTO.getAlternateName());
        existingProgram.setDescription(programDTO.getDescription());
        // If organization or other relationships require updating, include that logic here

        Program updatedProgram = programRepository.save(existingProgram);
        return updatedProgram.toDTO();
    }

    @Override
    public void deleteProgram(String id) {
        Program existingProgram = programRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));
        programRepository.delete(existingProgram);
    }

    private Program mapToEntity(ProgramDTO programDTO) {
        return Program.builder()
            .id(programDTO.getId())
            .name(programDTO.getName())
            .alternateName(programDTO.getAlternateName())
            .description(programDTO.getDescription())
            .build();
    }
}
