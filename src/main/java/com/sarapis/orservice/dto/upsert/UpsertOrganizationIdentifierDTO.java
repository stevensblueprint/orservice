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
                .identifierScheme(identifierScheme)
                .identifierType(identifierType)
                .identifier(identifier)
                .build();
    }
}
