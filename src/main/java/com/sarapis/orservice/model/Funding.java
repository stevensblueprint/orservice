package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.FUNDING_RESOURCE_TYPE;

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
@Table(name = "funding")
@Setter
@Getter
public class Funding {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "source")
  private String source;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadataRepository.saveAll(
        MetadataUtils.createMetadata(
            this,
            this,
            this.getId(),
            FUNDING_RESOURCE_TYPE,
            MetadataType.CREATE,
            updatedBy
        )
    );
  }
}
