package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Location;
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
@Table(name = "accessibility")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accessibility {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Location location;

  private String description;
  private String details;
  private String url;
}
