package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Location;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private Location location;

  private String attention;
  private String address_1;
  private String address_2;
  private String city;
  private String region;
  private String stateProvince;
  private String postalCode;
  private String country;

  @Enumerated(EnumType.STRING)
  private AddressType addressType;
}
