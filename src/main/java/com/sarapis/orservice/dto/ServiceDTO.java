package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private String id;
    private String name;
    private String alternateName;
    private String description;
    private String url;
    private List<UrlDTO> additionalUrls;
    private String email;
    private Status status;
    private String interpretationServices;
    private String applicationProcess;
    private String feesDescription;
    private String waitTime;
    private String fees;
    private String accreditations;
    private String eligibilityDescription;
    private int minimumAge;
    private int maximumAge;
    private LocalDate assuredDate;
    private String assurerEmail;
    private String licenses;
    private String alert;
    private LocalDateTime lastModified;
    private List<PhoneDTO> phones;
    private List<ScheduleDTO> schedules;
    private List<ServiceAreaDTO> serviceAreas;
    private List<ServiceAtLocationDTO> serviceAtLocations;
    private List<LanguageDTO> languages;
    private String organizationId;
    private List<FundingDTO> funding;
    private List<CostOptionDTO> costOptions;
    private String programId;
    private List<RequiredDocumentDTO> requiredDocuments;
    private List<ContactDTO> contacts;
    private List<ServiceCapacityDTO> capacities;
    private List<Object> attributes;
    private List<Object> metadata;
}
