package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.ServiceCapacity;
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
                .available(available)
                .maximum(maximum)
                .description(description)
                .updated(updated)
                .build();
    }

    public ServiceCapacity merge(ServiceCapacity serviceCapacity) {
        return ServiceCapacity.builder()
                .available(available == null ? serviceCapacity.getAvailable() : available)
                .maximum(maximum == null ? serviceCapacity.getMaximum() : maximum)
                .description(description == null ? serviceCapacity.getDescription() : description)
                .updated(updated == null ? serviceCapacity.getUpdated() : updated)
                .build();
    }
}
