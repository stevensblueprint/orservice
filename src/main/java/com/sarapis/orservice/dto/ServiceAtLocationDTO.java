package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.ServiceAtLocation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAtLocationDTO {
  private String id;
  private String description;
  private List<ServiceAreaDTO> serviceAreas = new ArrayList<>();
  private List<ContactDTO> contacts = new ArrayList<>();
  private List<PhoneDTO> phones = new ArrayList<>();
  private List<ScheduleDTO> schedules = new ArrayList<>();
  private LocationDTO location = null;
  private List<AttributeDTO> attributes = new ArrayList<>();
  private List<MetadataDTO> metadata = new ArrayList<>();

  public ServiceAtLocation toEntity() {
    return ServiceAtLocation.builder()
            .id(this.id)
            .description(this.description)
            .serviceAreas(this.serviceAreas.stream().map(ServiceAreaDTO::toEntity).toList())
            .contacts(this.contacts.stream().map(ContactDTO::toEntity).toList())
            .phones(this.phones.stream().map(PhoneDTO::toEntity).toList())
            .schedules(this.schedules.stream().map(ScheduleDTO::toEntity).toList())
            .location(this.location != null ? this.location.toEntity() : null)
            .attributes(this.attributes.stream().map(AttributeDTO::toEntity).toList())
            .metadata(this.metadata.stream().map(MetadataDTO::toEntity).toList())
            .build();
  }
}
