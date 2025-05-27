package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.LinkTypeDTO;
import com.sarapis.orservice.model.LinkType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LinkTypeMapper {
  LinkType toEntity(LinkTypeDTO.Request dto);
  LinkTypeDTO.Response toResponseDTO(LinkType entity);
}
