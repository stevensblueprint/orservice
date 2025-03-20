package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AREA_RESOURCE_TYPE;

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
@Table(name = "service_area")
@Getter
@Setter
public class ServiceArea {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "extent")
  private String extent;

  @Column(name = "extent_type")
  private String extentType;

  @Column(name = "uri")
  private String uri;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadataRepository.saveAll(
        MetadataUtils.createMetadata(
            this,
            this,
            this.getId(),
            SERVICE_AREA_RESOURCE_TYPE,
            MetadataType.CREATE,
            updatedBy
        )
    );
  }
}
