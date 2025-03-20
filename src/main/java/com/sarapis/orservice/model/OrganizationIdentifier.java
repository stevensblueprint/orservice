package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_IDENTIFIER_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization_identifier")
@Setter
@Getter
public class OrganizationIdentifier extends BaseResource {
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @Column(name = "identifier_scheme")
  private String identifierScheme;

  @Column(name = "identifier_type")
  private String identifierType;

  @Column(name = "identifier")
  private String identifier;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        ORGANIZATION_IDENTIFIER_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadata);
  }
}
