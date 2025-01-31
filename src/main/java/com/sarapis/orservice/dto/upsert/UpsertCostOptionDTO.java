package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.CostOption;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * The request body when inserting or updating a cost option entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertCostOptionDTO {
    private LocalDate validFrom;
    private LocalDate validTo;
    private String option;
    private String currency;
    private int amount;
    private String amountDescription;
    private String serviceId;

    public CostOption create() {
        return CostOption.builder()
                .id(UUID.randomUUID().toString())
                .validFrom(validFrom)
                .validTo(validTo)
                .option(option)
                .currency(currency)
                .amount(amount)
                .amountDescription(amountDescription)
                .build();
    }
}
