package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Program {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  private Organization organization;

  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  private String description;
}
