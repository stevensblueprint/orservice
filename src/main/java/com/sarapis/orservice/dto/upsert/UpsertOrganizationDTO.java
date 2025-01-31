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
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .email(this.email)
                .website(this.website)
                .taxStatus(this.taxStatus)
                .taxId(this.taxId)
                .yearIncorporated(this.yearIncorporated)
                .legalStatus(this.legalStatus)
                .logo(this.logo)
                .uri(this.uri)
                .build();
    }

    public Organization merge(Organization organization) {
        return Organization.builder()
                .id(organization.getId())
                .name(this.name == null ? organization.getName() : this.name)
                .alternateName(this.alternateName == null ? organization.getAlternateName() : this.alternateName)
                .description(this.description == null ? organization.getDescription() : this.description)
                .email(this.email == null ? organization.getEmail() : this.email)
                .website(this.website == null ? organization.getWebsite() : this.website)
                .taxStatus(this.taxStatus == null ? organization.getTaxStatus() : this.taxStatus)
                .taxId(this.taxId == null ? organization.getTaxId() : this.taxId)
                .yearIncorporated(this.yearIncorporated == null ? organization.getYearIncorporated() : this.yearIncorporated)
                .legalStatus(this.legalStatus == null ? organization.getLegalStatus() : this.legalStatus)
                .logo(this.logo == null ? organization.getLogo() : this.logo)
                .uri(this.uri == null ? organization.getUri() : this.uri)
                .childOrganizations(organization.getChildOrganizations())
                .services(organization.getServices())
                .parentOrganization(organization.getParentOrganization())
                .additionalWebsites(organization.getAdditionalWebsites())
                .funding(organization.getFunding())
                .contacts(organization.getContacts())
                .phones(organization.getPhones())
                .locations(organization.getLocations())
                .programs(organization.getPrograms())
                .organizationIdentifiers(organization.getOrganizationIdentifiers())
                .build();
    }
}
