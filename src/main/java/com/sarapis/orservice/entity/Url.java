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
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Url {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;
  private String label;
  private String url;

  @ManyToOne
  private Organization organization;

  @ManyToOne
  private Service service;

}
