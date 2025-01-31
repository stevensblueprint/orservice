package com.sarapis.orservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Returned response for a service capacity entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#service-capacity">Reference</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCapacityDTO {
    private String id;
    private String serviceId;
    private UnitDTO unit;
    private Integer available;
    private Integer maximum;
    private String description;
    private LocalDate updated;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();
}
