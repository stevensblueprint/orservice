package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cost_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CostOption {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @OneToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "valid_from")
  private LocalDate validFrom;

  @Column(name = "valid_to")
  private LocalDate validTo;

  @Column(name = "option")
  private String option;

  @Column(name = "currency")
  private String currency;

  @Column(name = "amount")
  private int amount;

  @Column(name = "amount_description")
  private String amountDescription;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
