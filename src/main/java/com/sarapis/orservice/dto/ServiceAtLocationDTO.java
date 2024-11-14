package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceAtLocationDTO {
  private String id;
  private ServiceDTO service;
  private Location location;
  private String description;
}
