package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.PROGRAM_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.ProgramDTO.Request;
import com.sarapis.orservice.dto.ProgramDTO.Response;
import com.sarapis.orservice.mapper.ProgramMapper;
import com.sarapis.orservice.model.Program;
import com.sarapis.orservice.repository.ProgramRepository;
import com.sarapis.orservice.utils.MetadataUtils;
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
  private final MetadataService metadataService;

  @Override
  public Response createProgram(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Program program = programMapper.toEntity(dto);
    Program savedProgram = programRepository.save(program);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedProgram.getId(),
        PROGRAM_RESOURCE_TYPE,
        CREATE.name(),
        "program",
        EMPTY_PREVIOUS_VALUE,
        dto.getName(),
        "SYSTEM"
    );
    ProgramDTO.Response response = programMapper.toResponseDTO(savedProgram);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        program.getId(), PROGRAM_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  public List<Response> getProgramsByOrganizationId(String organizationId) {
    List<Program> programs = programRepository.findByOrganizationId(organizationId);
    List<ProgramDTO.Response> programDtos = programs.stream().map(programMapper::toResponseDTO).collect(Collectors.toList());
    programDtos = programDtos.stream().peek(program -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          program.getId(), PROGRAM_RESOURCE_TYPE
      );
      program.setMetadata(metadata);
    }).toList();
    return programDtos;
  }
}
