package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.PROGRAM_RESOURCE_TYPE;

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
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "program")
@Getter
@Setter
public class Program {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @NotBlank
  @Column(name = "name")
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @NotBlank
  @Column(name = "description")
  private String description;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadataRepository.saveAll(MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        PROGRAM_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    ));
  }

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }

}
