package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.ServiceCapacity;
import com.sarapis.orservice.entity.Unit;
import com.sarapis.orservice.entity.core.Service;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCapacityDTO {
    private String id;

    private String serviceId;
    private String unitId;

    private int available;
    private int maximum;
    private String description;
    private LocalDate updated;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public ServiceCapacity toEntity(Service service, Unit unit) {
        return ServiceCapacity.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .service(service)
                .unit(unit)
                .available(this.available)
                .maximum(this.maximum)
                .description(this.description)
                .updated(this.updated)
                .build();
    }
}
