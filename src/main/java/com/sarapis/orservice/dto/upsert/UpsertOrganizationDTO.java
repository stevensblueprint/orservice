package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.util.Utility;
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
                .name(Utility.getOrDefault(this.name, organization.getName()))
                .alternateName(Utility.getOrDefault(this.alternateName, organization.getAlternateName()))
                .description(Utility.getOrDefault(this.description, organization.getDescription()))
                .email(Utility.getOrDefault(this.email, organization.getEmail()))
                .website(Utility.getOrDefault(this.website, organization.getWebsite()))
                .taxStatus(Utility.getOrDefault(this.taxStatus, organization.getTaxStatus()))
                .taxId(Utility.getOrDefault(this.taxId, organization.getTaxId()))
                .yearIncorporated(Utility.getOrDefault(this.yearIncorporated, organization.getYearIncorporated()))
                .legalStatus(Utility.getOrDefault(this.legalStatus, organization.getLegalStatus()))
                .logo(Utility.getOrDefault(this.logo, organization.getLogo()))
                .uri(Utility.getOrDefault(this.uri, organization.getUri()))
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
