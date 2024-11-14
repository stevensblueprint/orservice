package com.sarapis.orservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "meta_table_description")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetaTableDescription {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  private String name;
  private String language;
  @Column(name = "character_set")
  private String characterSet;
}
