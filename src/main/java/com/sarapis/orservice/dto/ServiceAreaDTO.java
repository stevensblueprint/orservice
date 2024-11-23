package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.ExtentType;
import com.sarapis.orservice.entity.ServiceArea;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAreaDTO {
    private String id;
    private String name;
    private String description;
    private String extent;
    private ExtentType extentType;
    private String uri;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public ServiceArea toEntity() {
        return ServiceArea.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .extent(this.extent)
                .extentType(this.extentType)
                .uri(this.uri)
                .build();
    }
}
