package com.sarapis.orservice.entity;

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
@Table(name = "required_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequiredDocument {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  private Service service;
  private String document;
  private String uri;
}
