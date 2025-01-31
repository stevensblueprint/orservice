package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.core.Organization;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The request body when inserting or updating an organization entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertOrganizationDTO {
    private String name;
    private String alternateName;
    private String description;
    private String email;
    private String website;
    private String taxStatus;
    private String taxId;
    private Integer yearIncorporated;
    private String legalStatus;
    private String logo;
    private String uri;
    private String parentOrganizationId;
    private List<String> additionalWebsites;
    private List<String> fundings;
    private List<String> contacts;
    private List<String> locations;
    private List<String> phones;
    private List<String> programs;
    private List<UpsertOrganizationIdentifierDTO> organizationIdentifiers;

    public Organization create() {
        return Organization.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .alternateName(alternateName)
                .description(description)
                .email(email)
                .website(website)
                .taxStatus(taxStatus)
                .taxId(taxId)
                .yearIncorporated(yearIncorporated == null ? null : yearIncorporated)
                .legalStatus(legalStatus)
                .logo(logo)
                .uri(uri)
                .build();
    }

    public Organization merge(Organization organization) {
        return Organization.builder()
                .name(name == null ? organization.getName() : name)
                .alternateName(alternateName == null ? organization.getAlternateName() : alternateName)
                .description(description == null ? organization.getDescription() : description)
                .email(email == null ? organization.getEmail() : email)
                .website(website == null ? organization.getWebsite() : website)
                .taxStatus(taxStatus == null ? organization.getTaxStatus() : taxStatus)
                .taxId(taxId == null ? organization.getTaxId() : taxId)
                .yearIncorporated(yearIncorporated == null ? organization.getYearIncorporated() : yearIncorporated)
                .legalStatus(legalStatus == null ? organization.getLegalStatus() : legalStatus)
                .logo(logo == null ? organization.getLogo() : logo)
                .uri(uri == null ? organization.getUri() : uri)
                .build();
    }
}
