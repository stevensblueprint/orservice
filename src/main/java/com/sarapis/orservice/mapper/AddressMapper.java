package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.ADDRESS_RESOURCE_TYPE;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.model.Address;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface AddressMapper {
  @Mapping(target = "location.id", source = "locationId")
  Address toEntity(AddressDTO.Request dto);

  @Mapping(target = "locationId", source = "location.id")
  AddressDTO.Response toResponseDTO(Address entity);

  @AfterMapping
  default void toEntity(AddressDTO.Request dto, @MappingTarget() Address entity) {
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
  }

  default AddressDTO.Response toResponseDTO(Address entity, MetadataService metadataService) {
    AddressDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(Address address, AddressDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            address.getId(), ADDRESS_RESOURCE_TYPE
        )
    );
  }
}
