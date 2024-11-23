package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.Organization;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationDTO {
    private String id;
    private String name;
    private String alternateName;
    private String description;
    private String email;
    private String website;
    private List<UrlDTO> additionalWebsites = new ArrayList<>();
    private String taxStatus;
    private String taxId;
    private int yearIncorporated;
    private String legalStatus;
    private String logo;
    private String uri;
    private OrganizationDTO parentOrganization = null;
    private List<FundingDTO> funding = new ArrayList<>();
    private List<ContactDTO> contacts = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<LocationDTO> locations = new ArrayList<>();
    private List<ProgramDTO> programs = new ArrayList<>();
    private List<OrganizationIdentifierDTO> organizationIdentifiers = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Organization toEntity() {
        return Organization.builder()
                .id(this.id)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .email(this.email)
                .website(this.website)
                .additionalWebsites(this.additionalWebsites.stream().map(UrlDTO::toEntity).toList())
                .taxStatus(this.taxStatus)
                .taxId(this.taxId)
                .yearIncorporated(this.yearIncorporated)
                .legalStatus(this.legalStatus)
                .logo(this.logo)
                .uri(this.uri)
                .parentOrganization(this.parentOrganization != null ? this.parentOrganization.toEntity() : null)
                .funding(this.funding.stream().map(FundingDTO::toEntity).toList())
                .contacts(this.contacts.stream().map(ContactDTO::toEntity).toList())
                .phones(this.phones.stream().map(PhoneDTO::toEntity).toList())
                .locations(this.locations.stream().map(LocationDTO::toEntity).toList())
                .programs(this.programs.stream().map(ProgramDTO::toEntity).toList())
                .organizationIdentifiers(this.organizationIdentifiers.stream().map(OrganizationIdentifierDTO::toEntity).toList())
                .build();
    }
}
