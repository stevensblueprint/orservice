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
    private int available;
    private int maximum;
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
}
