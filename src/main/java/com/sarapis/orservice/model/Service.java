package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service")
@Getter
@Setter
public class Service {
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "program_id")
  private Program program;

  @NotBlank
  @Column(name = "name")
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @Column(name = "description")
  private String description;

  @Column(name = "url")
  private String url;

  @Column(name = "email")
  private String email;

  @NotBlank
  @Column(name = "status")
  private String status;

  @Column(name = "interpretation_services")
  private String interpretationServices;

  @Column(name = "application_process")
  private String applicationProcess;

  @Column(name = "fees_description")
  private String feesDescription;

  @Column(name = "wait_time")
  private String waitTime;

  @Column(name = "fees")
  private String fees;

  @Column(name = "accreditations")
  private String accreditations;

  @Column(name = "eligibility_description")
  private String eligibility_description;

  @Column(name = "minimum_age")
  private Integer minimumAge;

  @Column(name = "maximum_age")
  private Integer maximumAge;

  @Column(name = "assured_date")
  private String assuredDate;

  @Column(name = "assurer_email")
  private String assurerEmail;

  @Column(name = "licenses")
  private String licenses;

  @Column(name = "alert")
  private String alert;

  @Column(name = "last_modified")
  private LocalDate lastModified;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Phone> phones = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Schedule> schedules= new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<ServiceArea> serviceAreas = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<ServiceAtLocation> serviceAtLocations = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Language> languages = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Funding> funding = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<CostOption> costOptions = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<RequiredDocument> requiredDocuments = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Contact> contacts = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Url> additionalUrls = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<ServiceCapacity> capacities = new ArrayList<>();


  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        SERVICE_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadata);

    this.getPhones().forEach(phone -> phone.setMetadata(metadataRepository, updatedBy));
    this.getSchedules().forEach(schedule -> schedule.setMetadata(metadataRepository, updatedBy));
    this.getServiceAreas().forEach(serviceArea -> serviceArea.setMetadata(metadataRepository, updatedBy));
    this.getServiceAtLocations().forEach(serviceAtLocation -> serviceAtLocation.setMetadata(metadataRepository, updatedBy));
    this.getLanguages().forEach(language -> language.setMetadata(metadataRepository, updatedBy));
    this.getFunding().forEach(funding -> funding.setMetadata(metadataRepository, updatedBy));
    this.getCostOptions().forEach(costOption -> costOption.setMetadata(metadataRepository, updatedBy));
    this.getRequiredDocuments().forEach(requiredDocument -> requiredDocument.setMetadata(metadataRepository, updatedBy));
    this.getContacts().forEach(contact -> contact.setMetadata(metadataRepository, updatedBy));
    this.getAdditionalUrls().forEach(url -> url.setMetadata(metadataRepository, updatedBy));
    this.getCapacities().forEach(capacity -> capacity.setMetadata(metadataRepository, updatedBy));
  }

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}
