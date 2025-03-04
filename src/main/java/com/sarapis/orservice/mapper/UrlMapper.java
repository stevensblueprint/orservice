package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Url;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface UrlMapper {
  Url toEntity(UrlDTO.Request dto);
  UrlDTO.Response toResponseDTO(Url entity);
}
