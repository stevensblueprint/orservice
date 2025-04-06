package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.PHONE_RESOURCE_TYPE;

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
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "phone")
@Getter
@Setter
public class Phone {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "contact_id")
  private Contact contact;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_at_location_id")
  private ServiceAtLocation serviceAtLocation;

  @NotBlank
  @Column(name = "number")
  private String number;

  @Column(name = "extension")
  private int extension;

  @Column(name = "type")
  private String type;

  @Column(name = "description")
  private String description;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "phone_id", referencedColumnName = "id")
  private List<Language> languages;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        PHONE_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadata);
    this.getLanguages().forEach(lang -> lang.setMetadata(metadataRepository, updatedBy));
  }

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}
