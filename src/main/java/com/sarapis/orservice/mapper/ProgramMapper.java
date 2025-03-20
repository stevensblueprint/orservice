package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.PROGRAM_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.model.Program;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProgramMapper {
  @Mapping(target = "organization.id", source = "organizationId")
  Program toEntity(ProgramDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  ProgramDTO.Response toResponseDTO(Program entity);

  @AfterMapping
  default void toEntity(ProgramDTO.Request dto, @MappingTarget() Program entity) {
    if (dto.getOrganizationId() == null) {
      entity.setOrganization(null);
    }
  }

  default ProgramDTO.Response toResponseDTO(Program entity, MetadataService metadataService) {
    ProgramDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(Program program, ProgramDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            program.getId(), PROGRAM_RESOURCE_TYPE
        )
    );
  }
}
