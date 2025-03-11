package com.sarapis.orservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "metadata")
@Getter
@Setter
public class Metadata {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "resource_id", nullable = false)
  private String resourceId;

  @Column(name = "resource_type", nullable = false)
  private String resourceType;

  @Column(name = "last_action_date", nullable = false)
  private LocalDate lastActionDate;

  @Column(name = "last_action_type", nullable = false)
  private String lastActionType;

  @Column(name = "field_name", nullable = false)
  private String fieldName;

  @Column(name = "previous_value", nullable = false)
  private String previousValue;

  @Column(name = "replacement_value", nullable = false)
  private String replacementValue;

  @Column(name = "updated_by", nullable = false)
  private String updatedBy;
}