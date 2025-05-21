package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.REQUIRED_DOCUMENT_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.RequiredDocumentDTO;
import com.sarapis.orservice.model.RequiredDocument;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface RequiredDocumentMapper {
  @Mapping(target = "service.id", source = "serviceId")
  RequiredDocument toEntity(RequiredDocumentDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  RequiredDocumentDTO.Response toResponseDTO(RequiredDocument entity);

  @AfterMapping
  default void toEntity(RequiredDocumentDTO.Response dto, @MappingTarget() RequiredDocument entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
  }

  default RequiredDocumentDTO.Response toResponseDTO(RequiredDocument entity, MetadataService metadataService) {
    RequiredDocumentDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        RequiredDocument::getId,
        RequiredDocumentDTO.Response::setMetadata,
        REQUIRED_DOCUMENT_RESOURCE_TYPE,
        metadataService
    );
    return response;
  }
}
