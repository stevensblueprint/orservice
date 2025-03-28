package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.TAXONOMY_TERM_RESOURCE_TYPE;

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
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "taxonomy_term")
@Getter
@Setter
@RequiredArgsConstructor
public class TaxonomyTerm {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "code", nullable = false)
  private String code;

  @NotBlank
  @Column(name = "name", nullable = false)
  private String name;

  @NotBlank
  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "parent_id")
  private TaxonomyTerm parent;

  @Column(name = "taxonomy")
  private String taxonomy;

  @ManyToOne
  @JoinColumn(name = "taxonomy_id")
  private Taxonomy taxonomyDetail;

  @Column
  private String language;

  @Column
  private String termUri;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadataList = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        TAXONOMY_TERM_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadataList);
  }

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}
