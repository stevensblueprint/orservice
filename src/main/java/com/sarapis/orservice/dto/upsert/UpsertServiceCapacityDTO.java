package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.ServiceCapacity;
import com.sarapis.orservice.util.Utility;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * The request body when inserting or updating a service capacity entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertServiceCapacityDTO {
    private Integer available;
    private Integer maximum;
    private String description;
    private LocalDate updated;
    private String serviceId;
    private String unitId;

    public ServiceCapacity create() {
        return ServiceCapacity.builder()
                .id(UUID.randomUUID().toString())
                .available(this.available)
                .maximum(this.maximum)
                .description(this.description)
                .updated(this.updated)
                .build();
    }

    public ServiceCapacity merge(ServiceCapacity serviceCapacity) {
        return ServiceCapacity.builder()
                .id(serviceCapacity.getId())
                .available(Utility.getOrDefault(this.available, serviceCapacity.getAvailable()))
                .maximum(Utility.getOrDefault(this.maximum, serviceCapacity.getMaximum()))
                .description(Utility.getOrDefault(this.description, serviceCapacity.getDescription()))
                .updated(Utility.getOrDefault(this.updated, serviceCapacity.getUpdated()))
                .service(serviceCapacity.getService())
                .unit(serviceCapacity.getUnit())
                .build();
    }
}
