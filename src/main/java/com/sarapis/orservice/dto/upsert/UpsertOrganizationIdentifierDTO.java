package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.OrganizationIdentifier;
import lombok.*;

import java.util.UUID;

/**
 * The request body when inserting or updating an organization identifier entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertOrganizationIdentifierDTO {
    private String identifierScheme;
    private String identifierType;
    private String identifier;
    private String organizationId;

    public OrganizationIdentifier create() {
        return OrganizationIdentifier.builder()
                .id(UUID.randomUUID().toString())
                .identifierScheme(this.identifierScheme)
                .identifierType(this.identifierType)
                .identifier(this.identifier)
                .build();
    }

    public OrganizationIdentifier merge(OrganizationIdentifier organizationIdentifier) {
        return OrganizationIdentifier.builder()
                .id(organizationIdentifier.getId())
                .identifierScheme(this.identifierScheme == null ? organizationIdentifier.getIdentifierScheme() : this.identifierScheme)
                .identifierType(this.identifierType == null ? organizationIdentifier.getIdentifierType() : this.identifierType)
                .identifier(this.identifier == null ? organizationIdentifier.getIdentifier() : this.identifier)
                .organization(organizationIdentifier.getOrganization())
                .build();
    }
}
