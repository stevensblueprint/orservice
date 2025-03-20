package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.REQUIRED_DOCUMENT_RESOURCE_TYPE;

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
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(RequiredDocument requiredDocument, RequiredDocumentDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            requiredDocument.getId(), REQUIRED_DOCUMENT_RESOURCE_TYPE
        )
    );
  }
}
