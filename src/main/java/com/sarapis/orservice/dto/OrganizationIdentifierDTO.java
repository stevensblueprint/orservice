package com.sarapis.orservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Returned response for organization identifier entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#contact">Reference</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationIdentifierDTO {
    private String id;
    private String organizationId;
    private String identifierScheme;
    private String identifierType;
    private String identifier;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();
}
