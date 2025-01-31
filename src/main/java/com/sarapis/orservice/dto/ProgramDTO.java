package com.sarapis.orservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Returned response for a program entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#program">Reference</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramDTO {
    private String id;
    private String organizationId;
    private String name;
    private String alternateName;
    private String description;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();
}
