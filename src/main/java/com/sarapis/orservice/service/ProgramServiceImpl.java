package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ProgramDTO.Request;
import com.sarapis.orservice.dto.ProgramDTO.Response;
import com.sarapis.orservice.mapper.ProgramMapper;
import com.sarapis.orservice.model.Program;
import com.sarapis.orservice.repository.ProgramRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService{

  private final ProgramRepository programRepository;
  private final ProgramMapper programMapper;

  @Override
  public Response createProgram(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Program program = programMapper.toEntity(dto);
    Program savedProgram = programRepository.save(program);
    return programMapper.toResponseDTO(savedProgram);
  }

  @Override
  public List<Response> getProgramsByOrganizationId(String organizationId) {
    List<Program> programs = programRepository.findByOrganizationId(organizationId);
    return programs.stream().map(programMapper::toResponseDTO).collect(Collectors.toList());
  }
}
