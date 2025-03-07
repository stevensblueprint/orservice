package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.validator.ValidUrl;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for Accessibility-related requests and responses.
 * <p>
 * This DTO is used to transfer accessibility data
 * between the client and server. It contains
 * nested static classes for request and
 * response objects, ensuring a clear
 * separation between the input and output
 * representations.
 * </p>
 *
 * @version 1.0
 */
public class AccessibilityDTO {

  /**
   * Represents the request structure for Accessibility data.
   * <p>
   * This class follows the snake_case JSON naming strategy and includes
   * fields required for creating or updating accessibility records.
   * </p>
   */
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {

    /**
     * The unique identifier of the accessibility record.
     */
    private String id;

    /**
     * The identifier of the associated location.
     */
    private String locationId;

    /**
     * A brief description of the accessibility feature.
     */
    private String description;

    /**
     * Additional details about the accessibility feature.
     */
    private String details;

    /**
     * A URL providing more information, validated using {@link ValidUrl}.
     */
    @ValidUrl
    private String url;
  }

  /**
   * Represents the response structure for Accessibility data.
   * <p>
   * This class follows the snake_case JSON naming strategy and contains
   * accessibility-related details, including attributes and metadata.
   * Some fields, like {@code locationId}, are ignored in the JSON output.
   * </p>
   */
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {

    /**
     * The unique identifier of the accessibility record.
     */
    private String id;

    /**
     * The identifier of the associated location (ignored in the JSON response).
     */
    @JsonIgnore
    private String locationId;

    /**
     * A brief description of the accessibility feature.
     */
    private String description;

    /**
     * Additional details about the accessibility feature.
     */
    private String details;

    /**
     * A URL providing more information about the accessibility feature.
     */
    private String url;

    /**
     * A list of attributes related to the accessibility feature.
     */
    private List<AttributeDTO.Response> attributes;

    /**
     * A list of metadata associated with the accessibility record.
     */
    private List<MetadataDTO.Response> metadata;
  }
}
