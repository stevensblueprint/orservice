package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @Column(name = "attention")
  private String attention;

  @Column(name = "address_1")
  private String address_1;

  @Column(name = "address_2")
  private String address_2;

  @Column(name = "city")
  private String city;

  @Column(name = "region")
  private String region;

  @Column(name = "state_province")
  private String stateProvince;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "country")
  private String country;

  @Enumerated(EnumType.STRING)
  @Column(name = "address_type")
  private AddressType addressType;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
