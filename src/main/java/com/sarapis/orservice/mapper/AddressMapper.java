package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface AddressMapper {
  Address toEntity(AddressDTO.Request dto);

  AddressDTO.Response toResponseDTO(Address entity);
}
