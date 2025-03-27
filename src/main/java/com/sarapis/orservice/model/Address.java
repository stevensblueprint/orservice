package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.ADDRESS_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "location_id")
  private Location location;

  @Column(name = "attention")
  private String attention;

  @NotBlank
  @Column(name = "address_1")
  private String address1;

  @Column(name = "address_2")
  private String address2;

  @NotBlank
  @Column(name = "city")
  private String city;

  @Column(name = "region")
  private String region;

  @NotBlank
  @Column(name = "state_province")
  private String stateProvince;

  @NotBlank
  @Column(name = "postal_code")
  private String postalCode;

  @NotBlank
  @Column(name = "country")
  private String country;

  @NotBlank
  @Enumerated(EnumType.STRING)
  @Column(name = "address_type")
  private AddressType addressType;

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        ADDRESS_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadata);
  }

  @PrePersist
  private void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}
