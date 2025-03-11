package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "location_id")
  private String locationId;

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

}
