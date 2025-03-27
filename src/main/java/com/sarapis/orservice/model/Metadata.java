package com.sarapis.orservice.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import lombok.ToString;

@Entity
@Table(name = "metadata")
@Getter
@Setter
public class Metadata {

  @Id
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

  @Column(name = "replacement_value", nullable = false, length = 1024)
  private String replacementValue;

  @Column(name = "updated_by", nullable = false)
  private String updatedBy;

  @Column(name = "file_import_id")
  private String fileImportId;

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}