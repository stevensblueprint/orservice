package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.ServiceCapacity;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCapacityDTO {
    private String id;
    private UnitDTO unit = null;
    private int available;
    private int maximum;
    private String description;
    private LocalDate updated;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public ServiceCapacity toEntity() {
        return ServiceCapacity.builder()
                .id(this.id)
                .unit(this.unit != null ? this.unit.toEntity() : null)
                .available(this.available)
                .maximum(this.maximum)
                .description(this.description)
                .updated(this.updated)
                .build();
    }
}
