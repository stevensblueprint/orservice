package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.model.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface PhoneMapper {
  Phone toEntity(PhoneDTO.Request dto);

  PhoneDTO.Response toResponseDTO(Phone entity);
}
