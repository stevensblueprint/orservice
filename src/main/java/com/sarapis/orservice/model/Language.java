package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.LANGUAGE_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "language")
@Setter
@Getter
public class Language {
  @Id
  @Column(name = "id", nullable = false, insertable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "phone_id")
  private Phone phone;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "note")
  private String note;

  public void setMetadata(MetadataRepository metadata, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadata.saveAll(MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        LANGUAGE_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    ));
  }
}
