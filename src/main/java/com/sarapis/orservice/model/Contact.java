package com.sarapis.orservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
public class Contact {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

 @Column(name = "organization_id")
 private String organizationId;

 @Column(name = "service_id")
 private String serviceId;

 @Column(name = "service_at_location_id")
 private String serviceAtLocationId;

 @Column(name = "location_id")
 private String locationId;

 @Column(name = "name")
  private String name;

 @Column(name = "title")
  private String title;

 @Column(name = "department")
  private String department;

 @Column(name = "email")
  private String email;

 @OneToMany
 @JoinColumn(name = "contact_id", referencedColumnName = "id")
 private List<Phone> phones;
}
