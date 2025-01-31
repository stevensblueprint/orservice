package com.sarapis.orservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Returned response for organization entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#organization">Reference</a>
 */
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
    private Integer yearIncorporated;
    private String legalStatus;
    private String logo;
    private String uri;
    private String parentOrganizationId;
    private List<FundingDTO> funding = new ArrayList<>();
    private List<ContactDTO> contacts = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<LocationDTO> locations = new ArrayList<>();
    private List<ProgramDTO> programs = new ArrayList<>();
    private List<OrganizationIdentifierDTO> organizationIdentifiers = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();
}