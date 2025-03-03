package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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

  @Column(name = "service_id")
  private String serviceId;

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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "link_id", referencedColumnName = "id")
  private List<Attribute> attributes;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "resource_id", referencedColumnName = "id")
  private List<Metadata> metadata;
}
