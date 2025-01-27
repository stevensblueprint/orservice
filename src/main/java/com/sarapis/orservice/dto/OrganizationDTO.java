package com.sarapis.orservice.dto;

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
public class OrganizationDTO {
    private String id;

    private String parentOrganizationId;

    private String name;
    private String alternateName;
    private String description;
    private String email;
    private String website;
    private String taxStatus;
    private String taxId;
    private int yearIncorporated;
    private String legalStatus;
    private String logo;
    private String uri;

    private List<UrlDTO> additionalWebsites = new ArrayList<>();
    private List<FundingDTO> funding = new ArrayList<>();
    private List<ContactDTO> contacts = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<LocationDTO> locations = new ArrayList<>();
    private List<ProgramDTO> programs = new ArrayList<>();
    private List<OrganizationIdentifierDTO> organizationIdentifiers = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Organization toEntity(Organization parentOrganization) {
        Organization organization = Organization.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .parentOrganization(parentOrganization)
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
        organization.setAdditionalWebsites(this.additionalWebsites.stream().map(e -> e.toEntity(organization, null)).toList());
        organization.setFunding(this.funding.stream().map(e -> e.toEntity(organization, null)).toList());
        organization.setContacts(this.contacts.stream().map(e -> e.toEntity(organization, null, null, null)).toList());
        organization.setPhones(this.phones.stream().map(e -> e.toEntity(null, null, organization, null, null)).toList());
        organization.setLocations(this.locations.stream().map(e -> e.toEntity(organization)).toList());
        organization.setPrograms(this.programs.stream().map(e -> e.toEntity(organization)).toList());
        organization.setOrganizationIdentifiers(this.organizationIdentifiers.stream().map(e -> e.toEntity(organization)).toList());
        return organization;
    }
}
