package com.sarapis.orservice.model;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseResource {
  @Id
  private String id;

  @PrePersist
  public void prePersist() {
    this.id = StringUtils.isEmpty(id)? UUID.randomUUID().toString() : id;
  }
}
