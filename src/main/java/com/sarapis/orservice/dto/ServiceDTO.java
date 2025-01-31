package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Returned response for a service entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#service">Reference</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDTO {
    private String id;
    private String name;
    private String alternateName;
    private String description;
    private String url;
    private List<UrlDTO> additionalUrls = new ArrayList<>();
    private String email;
    private Status status;
    private String interpretationServices;
    private String applicationProcess;
    private String feesDescription;
    private String waitTime;
    private String fees;
    private String accreditations;
    private String eligibilityDescription;
    private Integer minimumAge;
    private Integer maximumAge;
    private LocalDate assuredDate;
    private String assurerEmail;
    private String licenses;
    private String alert;
    private LocalDateTime lastModified;
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<ScheduleDTO> schedules = new ArrayList<>();
    private List<ServiceAreaDTO> serviceAreas = new ArrayList<>();
    private List<ServiceAtLocationDTO> serviceAtLocations = new ArrayList<>();
    private List<LanguageDTO> languages = new ArrayList<>();
    private String organizationId;
    private List<FundingDTO> funding = new ArrayList<>();
    private List<CostOptionDTO> costOptions = new ArrayList<>();
    private String programId;
    private List<RequiredDocumentDTO> requiredDocuments = new ArrayList<>();
    private List<ContactDTO> contacts = new ArrayList<>();
    private List<ServiceCapacityDTO> capacities = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();
}
