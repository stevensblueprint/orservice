package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Contact;
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
public class ContactDTO {
    private String id;

    private String organizationId;
    private String serviceId;
    private String serviceAtLocationId;
    private String locationId;

    private String name;
    private String title;
    private String department;
    private String email;

    private List<PhoneDTO> phones = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Contact toEntity(Organization organization,
                            Service service,
                            ServiceAtLocation serviceAtLocation,
                            Location location) {
        Contact contact = Contact.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .organization(organization)
                .service(service)
                .serviceAtLocation(serviceAtLocation)
                .location(location)
                .name(this.name)
                .title(this.title)
                .department(this.department)
                .email(this.email)
                .build();
        contact.setPhones(this.phones.stream().map(e -> e.toEntity(null, null, null, contact, null)).toList());
        return contact;
    }
}
