package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.OrganizationIdentifier;
import com.sarapis.orservice.util.Utility;
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
                .identifierScheme(Utility.getOrDefault(this.identifierScheme, organizationIdentifier.getIdentifierScheme()))
                .identifierType(Utility.getOrDefault(this.identifierType, organizationIdentifier.getIdentifierType()))
                .identifier(Utility.getOrDefault(this.identifier, organizationIdentifier.getIdentifier()))
                .organization(organizationIdentifier.getOrganization())
                .build();
    }
}
