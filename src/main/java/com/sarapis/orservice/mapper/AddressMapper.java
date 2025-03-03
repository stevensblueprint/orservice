package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface AddressMapper {
  Address toEntity(AddressDTO.Request dto);

  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "metadata", source = "metadata")
  AddressDTO.Response toResponseDTO(Address entity);
}
