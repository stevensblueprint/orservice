package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.util.Utility;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The request body when inserting or updating a contact entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertContactDTO {
    private String id;
    private String name;
    private String title;
    private String department;
    private String email;
    private String organizationId;
    private String serviceId;
    private String serviceAtLocationId;
    private String locationId;
    private List<String> phones;

    public Contact create() {
        return Contact.builder()
                .id(UUID.randomUUID().toString())
                .name(this.name)
                .title(this.title)
                .department(this.department)
                .email(this.email)
                .build();
    }

    public Contact merge(Contact contact) {
        return Contact.builder()
                .id(contact.getId())
                .name(Utility.getOrDefault(this.name, contact.getName()))
                .title(Utility.getOrDefault(this.title, contact.getTitle()))
                .department(Utility.getOrDefault(this.department, contact.getDepartment()))
                .email(Utility.getOrDefault(this.email, contact.getEmail()))
                .organization(contact.getOrganization())
                .service(contact.getService())
                .serviceAtLocation(contact.getServiceAtLocation())
                .location(contact.getLocation())
                .phones(contact.getPhones())
                .build();
    }
}
