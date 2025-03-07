/**
 * This package contains the entity models for the Sarapis OR Service.
 * <p>
 * The entities in this package are mapped to database tables using
 * Jakarta Persistence (JPA) annotations. These models represent
 * different data structures used within the application.
 * </p>
 *
 * @version 1.0
 */
package com.sarapis.orservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an accessibility entity that provides information regarding
 * the accessibility features or details associated with a specific location.
 * <p>
 * This entity is mapped to the "accessibility" table in the database using
 * Jakarta Persistence (JPA) annotations. Lombok is used to automatically
 * generate the getter and setter methods for the fields.
 * </p>
 * <p>
 * Each instance includes:
 * <ul>
 *   <li><b>id</b>: The unique identifier of the accessibility record.</li>
 *   <li><b>locationId</b>: The identifier linking to a specific location.</li>
 *   <li><b>description</b>: A brief description of the accessibility.</li>
 *   <li><b>details</b>: Additional details regarding the accessibility.</li>
 *   <li><b>url</b>: A URL providing more information or resources.</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "accessibility")
@Getter
@Setter
public class Accessibility {

  /**
   * The unique identifier for the accessibility record.
   */
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  /**
   * Identifier for the associated location.
   */
  @Column(name = "location_id")
  private String locationId;

  /**
   * A short description of the accessibility.
   */
  @Column(name = "description")
  private String description;

  /**
   * Additional details regarding the accessibility.
   */
  @Column(name = "details")
  private String details;

  /**
   * A URL that provides further information about the accessibility.
   */
  @Column(name = "url")
  private String url;
}
