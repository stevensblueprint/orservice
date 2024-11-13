package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "funding")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Funding {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Organization organization;

  @ManyToOne
  private Service service;

  private String source;


}
