package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Setter
@Getter
public class Organization extends BaseResource {
  @NotBlank
  @Column(name = "name")
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @NotBlank
  @Column(name = "description")
  private String description;

  @Column(name = "email")
  private String email;

  @Column(name = "website")
  private String website;

  @Column(name = "tax_status")
  private String taxStatus;

  @Column(name = "tax_id")
  private String taxId;

  @Column(name = "year_incorporated")
  private Integer yearIncorporated;

  @Column(name = "legal_status")
  private String legalStatus;

  @Column(name = "logo")
  private String logo;

  @Column(name = "uri")
  private String uri;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Url> additionalWebsites = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Funding> funding = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Contact> contacts = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Phone> phones = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Program> programs = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<OrganizationIdentifier> organizationIdentifiers = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Location> locations = new ArrayList<>();

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    List<Metadata> metadata = MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        ORGANIZATION_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    );
    metadataRepository.saveAll(metadata);

    this.getOrganizationIdentifiers().forEach(identifier -> identifier.setMetadata(metadataRepository, updatedBy));
    this.getAdditionalWebsites().forEach(website -> website.setMetadata(metadataRepository, updatedBy));
    this.getLocations().forEach(location -> location.setMetadata(metadataRepository, updatedBy));
    this.getContacts().forEach(contact -> contact.setMetadata(metadataRepository, updatedBy));
    this.getPhones().forEach(phone -> phone.setMetadata(metadataRepository, updatedBy));
    this.getPrograms().forEach(program -> program.setMetadata(metadataRepository, updatedBy));
    this.getFunding().forEach(funding -> funding.setMetadata(metadataRepository, updatedBy));
  }

}
