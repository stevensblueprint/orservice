package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.OrganizationIdentifier;
import com.sarapis.orservice.entity.core.Organization;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public OrganizationIdentifier toEntity(Organization organization) {
        return OrganizationIdentifier.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .organization(organization)
                .identifierScheme(this.identifierScheme)
                .identifierType(this.identifierType)
                .identifier(this.identifier)
                .build();
    }
}
