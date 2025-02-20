package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {
    private String id;
    private String organizationId;
    private String name;
    private String alternateName;
    private String description;
    private List<Object> attributes;
    private List<Object> metadata;
}
