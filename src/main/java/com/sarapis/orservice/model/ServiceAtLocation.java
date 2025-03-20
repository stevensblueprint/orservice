package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AT_LOCATION_RESOURCE_TYPE;

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
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_at_location")
@Setter
@Getter
public class ServiceAtLocation {
  @Id
  @Column(name = "id", insertable = false, updatable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id")
  private Service service;

  private String description;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private List<Contact> contacts;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private List<Phone> phones;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private List<Schedule> schedules;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        SERVICE_AT_LOCATION_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadata);

    this.getContacts().forEach(contact -> contact.setMetadata(metadataRepository, updatedBy));
    this.getPhones().forEach(phone -> phone.setMetadata(metadataRepository, updatedBy));
    this.getSchedules().forEach(schedule -> schedule.setMetadata(metadataRepository, updatedBy));
  }
}
