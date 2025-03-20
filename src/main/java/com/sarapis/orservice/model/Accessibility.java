package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.ACCESSIBILITY_RESOURCE_TYPE;

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
import org.hibernate.annotations.Where;

@Entity
@Table(name = "accessibility")
@Getter
@Setter
public class Accessibility {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "location_id")
  private Location location;

  @Column(name = "description")
  private String description;

  @Column(name = "details")
  private String details;

  @Column(name = "url")
  private String url;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadataRepository.saveAll(MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        ACCESSIBILITY_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    ));
  }
}
