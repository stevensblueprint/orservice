package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.Url;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "organization")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  private String description;

  private String email;

  private String website;

  @OneToMany
  private List<Url> additionalWebsites;

  @Column(name = "year_incorporated")
  private int yearIncorporated;

  private String legalStatus;

  private String logo;

  private String uri;

  @OneToOne
  private Organization parentOrganization;

  public OrganizationDTO toDTO() {
    return OrganizationDTO.builder()
       .id(id)
       .name(name)
       .alternateName(alternateName)
       .description(description)
       .email(email)
       .website(website)
       .additionalWebsites(additionalWebsites)
       .yearIncorporated(yearIncorporated)
       .legalStatus(legalStatus)
       .logo(logo)
       .uri(uri)
       .parentOrganization(parentOrganization)
       .build();
  }
}
