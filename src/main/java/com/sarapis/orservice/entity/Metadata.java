package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  // FK Property
  @Column(name = "resource_id", nullable = false, unique = true)
  private String resourceId;

  @Enumerated(EnumType.STRING)
  @Column(name = "resource_type")
  private ResourceType resourceType;

  @Column(name = "last_action_date")
  private LocalDate lastActionDate;

  @Column(name = "last_action_type")
  private String lastActionType;

  @Column(name = "field_name")
  private String fieldName;

  @Column(name = "previous_value")
  private String previousValue;

  @Column(name = "replacement_value")
  private String replacementValue;

  @Column(name = "updated_by")
  private String updatedBy;
}
