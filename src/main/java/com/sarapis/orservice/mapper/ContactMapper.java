package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.CONTACT_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.model.Contact;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class ContactMapper {
  @Autowired
  private PhoneMapper phoneMapper;

  @Mapping(target = "organization.id", source = "organizationId")
  @Mapping(target = "service.id", source = "serviceId")
  @Mapping(target = "serviceAtLocation.id", source = "serviceAtLocationId")
  @Mapping(target = "location.id", source = "locationId")
  public abstract Contact toEntity(ContactDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "serviceId", source = "service.id")
  @Mapping(target = "serviceAtLocationId", source = "serviceAtLocation.id")
  @Mapping(target = "locationId", source = "location.id")
  public abstract  ContactDTO.Response toResponseDTO(Contact entity);

  @AfterMapping
  protected void toEntity(ContactDTO.Request dto, @MappingTarget() Contact entity) {
    if (dto.getOrganizationId() != null) {
      entity.setOrganization(null);
    }
    if (dto.getServiceId() != null) {
      entity.setService(null);
    }
    if (dto.getServiceAtLocationId() != null) {
      entity.setServiceAtLocation(null);
    }
    if (dto.getLocationId() != null) {
      entity.setLocation(null);
    }

    if (entity.getPhones() != null) {
      entity.getPhones().forEach(phone -> phone.setContact(entity));
    }
  }

  public ContactDTO.Response toResponseDTO(Contact entity, MetadataService metadataService) {
    ContactDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    if (entity.getPhones() != null) {
      List<PhoneDTO.Response> enrichedPhones =
          entity.getPhones().stream()
             .map(phone -> phoneMapper.toResponseDTO(phone, metadataService)).toList();
      response.setPhones(enrichedPhones);
    }
    return response;
  }

  protected void enrichMetadata(Contact contact, ContactDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            contact.getId(), CONTACT_RESOURCE_TYPE
        )
    );
  }
}
