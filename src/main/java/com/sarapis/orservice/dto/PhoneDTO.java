package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDTO {
    private String id;

    private String locationId;
    private String serviceId;
    private String organizationId;
    private String contactId;
    private String serviceAtLocationId;

    private String number;
    private String extension;
    private String type;
    private String description;

    private List<LanguageDTO> languages = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Phone toEntity(Location location,
                          Service service,
                          Organization organization,
                          Contact contact,
                          ServiceAtLocation serviceAtLocation) {
        Phone phone = Phone.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .location(location)
                .service(service)
                .organization(organization)
                .contact(contact)
                .serviceAtLocation(serviceAtLocation)
                .number(this.number)
                .extension(this.extension)
                .type(this.type)
                .description(this.description)
                .build();
        phone.setLanguages(this.languages.stream().map(e -> e.toEntity(null, null, phone)).toList());
        return phone;
    }
}
