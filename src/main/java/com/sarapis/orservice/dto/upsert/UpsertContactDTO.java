package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.Contact;
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
                .name(name)
                .title(title)
                .department(department)
                .email(email)
                .build();
    }
}
