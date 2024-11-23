package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
  private int minimumAge;
  private int maximumAge;
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
  private OrganizationDTO organization;
  private List<FundingDTO> funding = new ArrayList<>();
  private List<CostOptionDTO> costOptions = new ArrayList<>();
  private ProgramDTO program = null;
  private List<RequiredDocumentDTO> requiredDocuments = new ArrayList<>();
  private List<ContactDTO> contacts = new ArrayList<>();
  private List<ServiceCapacityDTO> capacities = new ArrayList<>();
  private List<AttributeDTO> attributes = new ArrayList<>();
  private List<MetadataDTO> metadata = new ArrayList<>();

  public Service toEntity() {
    return Service.builder()
            .id(this.id)
            .name(this.name)
            .alternateName(this.alternateName)
            .description(this.description)
            .url(this.url)
            .additionalUrls(this.additionalUrls.stream().map(UrlDTO::toEntity).toList())
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
            .phones(this.phones.stream().map(PhoneDTO::toEntity).toList())
            .schedules(this.schedules.stream().map(ScheduleDTO::toEntity).toList())
            .serviceAreas(this.serviceAreas.stream().map(ServiceAreaDTO::toEntity).toList())
            .serviceAtLocations(this.serviceAtLocations.stream().map(ServiceAtLocationDTO::toEntity).toList())
            .languages(this.languages.stream().map(LanguageDTO::toEntity).toList())
            .organization(this.organization.toEntity())
            .funding(this.funding.stream().map(FundingDTO::toEntity).toList())
            .costOptions(this.costOptions.stream().map(CostOptionDTO::toEntity).toList())
            .program(this.program != null ? this.program.toEntity() : null)
            .requiredDocuments(this.requiredDocuments.stream().map(RequiredDocumentDTO::toEntity).toList())
            .contacts(this.contacts.stream().map(ContactDTO::toEntity).toList())
            .capacities(this.capacities.stream().map(ServiceCapacityDTO::toEntity).toList())
            .build();
  }
}
