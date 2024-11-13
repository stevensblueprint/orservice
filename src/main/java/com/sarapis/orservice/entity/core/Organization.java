package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.entity.Url;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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


}
