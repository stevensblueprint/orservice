package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.RequiredDocumentDTO;
import com.sarapis.orservice.model.RequiredDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface RequiredDocumentMapper {
  RequiredDocument toEntity(RequiredDocumentDTO.Request dto);

  RequiredDocumentDTO.Response toResponseDTO(RequiredDocument entity);
}
