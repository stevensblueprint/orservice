package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.PROGRAM_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.PROGRAM_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.model.Program;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProgramMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;
  @Mapping(target = "organization.id", source = "organizationId")
  public abstract Program toEntity(ProgramDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  public abstract ProgramDTO.Response toResponseDTO(Program entity);

  @AfterMapping
  protected void toEntity(ProgramDTO.Request dto, @MappingTarget() Program entity) {
    if (dto.getOrganizationId() == null) {
      entity.setOrganization(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        PROGRAM_LINK_TYPE
    );
  }

  protected ProgramDTO.Response toResponseDTO(Program entity, MetadataService metadataService) {
    ProgramDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    enrichAttributes(
        entity,
        response,
        Program::getId,
        ProgramDTO.Response::setAttributes,
        PROGRAM_LINK_TYPE,
        attributeService
    );
    return response;
  }

  private void enrichMetadata(Program program, ProgramDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            program.getId(), PROGRAM_RESOURCE_TYPE
        )
    );
  }
}
