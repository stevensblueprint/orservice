package com.sarapis.orservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "attribute")
public class Attribute {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

}
