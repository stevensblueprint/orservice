package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_CAPACITY_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="service_capacity")
@Getter
@Setter
public class ServiceCapacity {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "unit_id")
  private Unit unit;

  private int available;
  private int maximum;
  private String description;
  private LocalDate updated;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        SERVICE_CAPACITY_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );

    metadataRepository.saveAll(metadata);
  }

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}
