package com.sarapis.orservice.dto;

import lombok.*;

import java.util.List;

/**
 * Returned response for a contact entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#contact">Reference</a>
 */
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
    private List<PhoneDTO> phones;
    private List<AttributeDTO> attributes;
    private List<MetadataDTO> metadata;
}
