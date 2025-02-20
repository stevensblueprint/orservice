package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private String id;
    private String name;
    private String title;
    private String department;
    private String email;

    private String organizationId;
    private String serviceId;
    private String serviceAtLocationId;
    private String locationId;

    // Assuming PhoneDTO is defined elsewhere
    private List<PhoneDTO> phones;

    // Additional attributes/metadata if needed
    private List<Object> attributes;
    private List<Object> metadata;
}
