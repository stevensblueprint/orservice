package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.LOCATION_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "location")
@Setter
@Getter
public class Location {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @NotBlank
  @Enumerated(EnumType.STRING)
  @Column(name = "location_type")
  private LocationType locationType;

  @Column(name = "url")
  private String url;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @Column(name = "name")
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @Column(name = "description")
  private String description;

  @Column(name = "transportation")
  private String transportation;

  @Column(name = "latitude")
  private String latitude;

  @Column(name = "longitude")
  private String longitude;

  @Column(name = "external_identifier")
  private String externalIdentifier;

  @Column(name = "external_identifier_type")
  private String externalIdentifierType;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private List<Language> languages;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private List<Address> addresses;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private List<Contact> contacts;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private List<Accessibility> accessibility;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private List<Phone> phones;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private List<Schedule> schedules;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        LOCATION_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadata);
    this.getLanguages().forEach(lang -> lang.setMetadata(metadataRepository, updatedBy));
    this.getAddresses().forEach(address -> address.setMetadata(metadataRepository, updatedBy));
    this.getContacts().forEach(contact -> contact.setMetadata(metadataRepository, updatedBy));
    this.getAccessibility().forEach(access -> access.setMetadata(metadataRepository, updatedBy));
    this.getPhones().forEach(phone -> phone.setMetadata(metadataRepository, updatedBy));
    this.getSchedules().forEach(schedule -> schedule.setMetadata(metadataRepository, updatedBy));
  }
}
