package com.sarapis.orservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "resource_id", nullable = false, unique = true)
  private UUID resourceId;

  @Enumerated(EnumType.STRING)
  private ResourceType resourceType;

  @Column(name = "last_action_date")
  private LocalDate lastActionDate;

  @Column(name = "last_action_type")
  private String lastActionType;

  private String fieldName;

  private String previousValue;

  private String replacementValue;

  private String updatedBy;
}
