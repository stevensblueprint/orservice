package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.ADDRESS_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.ADDRESS_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.model.Address;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class AddressMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "location.id", source = "locationId")
  public abstract Address toEntity(AddressDTO.Request dto);

  @Mapping(target = "locationId", source = "location.id")
  public abstract AddressDTO.Response toResponseDTO(Address entity);

  @AfterMapping
  protected void toEntity(AddressDTO.Request dto, @MappingTarget() Address entity) {
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        ADDRESS_LINK_TYPE
    );
  }

  protected AddressDTO.Response toResponseDTO(Address entity, MetadataService metadataService) {
    AddressDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Address::getId,
        AddressDTO.Response::setMetadata,
        ADDRESS_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        Address::getId,
        AddressDTO.Response::setAttributes,
        ADDRESS_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
