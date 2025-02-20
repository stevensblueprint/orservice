package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private List<UrlDTO> additionalWebsites;
    private List<FundingDTO> funding;
    private List<ContactDTO> contacts;
    private List<PhoneDTO> phones;
    private List<LocationDTO> locations;
    private List<ProgramDTO> programs;
    private List<OrganizationIdentifierDTO> organizationIdentifiers;
    private List<Object> attributes;
    private List<Object> metadata;
}
