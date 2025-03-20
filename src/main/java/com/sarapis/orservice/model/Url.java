package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "url")
@Getter
@Setter
public class Url extends BaseResource {
  @Column(name = "label")
  private String label;

  @Column(name = "url", nullable = false)
  private String url;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  public void setMetadata(MetadataRepository metadata, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadata.saveAll(MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        URL_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    ));
  }
}
