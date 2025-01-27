package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDTO {
    private String id;

    private String organizationId;
    private String programId;

    private String name;
    private String alternateName;
    private String description;
    private String url;
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

    private List<UrlDTO> additionalUrls = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<ScheduleDTO> schedules = new ArrayList<>();
    private List<ServiceAreaDTO> serviceAreas = new ArrayList<>();
    private List<ServiceAtLocationDTO> serviceAtLocations = new ArrayList<>();
    private List<LanguageDTO> languages = new ArrayList<>();
    private List<FundingDTO> funding = new ArrayList<>();
    private List<CostOptionDTO> costOptions = new ArrayList<>();
    private List<RequiredDocumentDTO> requiredDocuments = new ArrayList<>();
    private List<ContactDTO> contacts = new ArrayList<>();
    private List<ServiceCapacityDTO> capacities = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Service toEntity(Organization organization, Program program) {
        Service service = Service.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .organization(organization)
                .program(program)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .url(this.url)
                .email(this.email)
                .status(this.status)
                .interpretationServices(this.interpretationServices)
                .applicationProcess(this.applicationProcess)
                .feesDescription(this.feesDescription)
                .waitTime(this.waitTime)
                .fees(this.fees)
                .accreditations(this.accreditations)
                .eligibilityDescription(this.eligibilityDescription)
                .minimumAge(this.minimumAge)
                .maximumAge(this.maximumAge)
                .assuredDate(this.assuredDate)
                .assurerEmail(this.assurerEmail)
                .licenses(this.licenses)
                .alert(this.alert)
                .lastModified(this.lastModified)
                .build();
        service.setAdditionalUrls(this.additionalUrls.stream().map(e -> e.toEntity(null, service)).toList());
        service.setPhones(this.phones.stream().map(e -> e.toEntity(null, service, null, null, null)).toList());
        service.setSchedules(this.schedules.stream().map(e -> e.toEntity(service, null, null)).toList());
        service.setServiceAreas(this.serviceAreas.stream().map(e -> e.toEntity(service, null)).toList());
        service.setServiceAtLocations(this.serviceAtLocations.stream().map(e -> e.toEntity(service, null)).toList());
        service.setLanguages(this.languages.stream().map(e -> e.toEntity(service, null, null)).toList());
        service.setFunding(this.funding.stream().map(e -> e.toEntity(null, service)).toList());
        service.setCostOptions(this.costOptions.stream().map(e -> e.toEntity(service)).toList());
        service.setRequiredDocuments(this.requiredDocuments.stream().map(e -> e.toEntity(service)).toList());
        service.setContacts(this.contacts.stream().map(e -> e.toEntity(null, service, null, null)).toList());
        service.setCapacities(this.capacities.stream().map(e -> e.toEntity(service, null)).toList());
        return service;
    }
}
