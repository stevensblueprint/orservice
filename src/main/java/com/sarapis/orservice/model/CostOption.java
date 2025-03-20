package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.COST_OPTION_RESOURCE_TYPE;

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
@Table(name = "cost_option")
@Getter
@Setter
public class CostOption {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "valid_from")
  private String validFrom;

  @Column(name = "valid_to")
  private String validTo;

  @Column(name = "option")
  private String option;

  @Column(name = "currency")
  private String currency;

  @Column(name = "amount")
  private Integer amount;

  @Column(name = "amount_description")
  private String amountDescription;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadataRepository.saveAll(MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        COST_OPTION_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    ));
  }
}
