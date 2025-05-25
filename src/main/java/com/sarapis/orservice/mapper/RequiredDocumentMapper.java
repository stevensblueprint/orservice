package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.REQUIRED_DOCUMENT_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.REQUIRED_DOCUMENT_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.RequiredDocumentDTO;
import com.sarapis.orservice.model.RequiredDocument;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class RequiredDocumentMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;
  @Mapping(target = "service.id", source = "serviceId")
  public abstract RequiredDocument toEntity(RequiredDocumentDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  public abstract RequiredDocumentDTO.Response toResponseDTO(RequiredDocument entity);

  @AfterMapping
  protected void toEntity(RequiredDocumentDTO.Request dto, @MappingTarget() RequiredDocument entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        REQUIRED_DOCUMENT_LINK_TYPE
    );
  }

  protected RequiredDocumentDTO.Response toResponseDTO(RequiredDocument entity, MetadataService metadataService) {
    RequiredDocumentDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        RequiredDocument::getId,
        RequiredDocumentDTO.Response::setMetadata,
        REQUIRED_DOCUMENT_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        RequiredDocument::getId,
        RequiredDocumentDTO.Response::setAttributes,
        REQUIRED_DOCUMENT_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
