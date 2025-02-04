package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.CostOption;
import com.sarapis.orservice.util.Utility;
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
    private Integer amount;
    private String amountDescription;
    private String serviceId;

    public CostOption create() {
        return CostOption.builder()
                .id(UUID.randomUUID().toString())
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .option(this.option)
                .currency(this.currency)
                .amount(this.amount)
                .amountDescription(this.amountDescription)
                .build();
    }

    public CostOption merge(CostOption costOption) {
        return CostOption.builder()
                .id(costOption.getId())
                .validFrom(Utility.getOrDefault(this.validFrom, costOption.getValidFrom()))
                .validTo(Utility.getOrDefault(this.validTo, costOption.getValidTo()))
                .option(Utility.getOrDefault(this.option, costOption.getOption()))
                .currency(Utility.getOrDefault(this.currency, costOption.getCurrency()))
                .amount(Utility.getOrDefault(this.amount, costOption.getAmount()))
                .amountDescription(Utility.getOrDefault(this.amountDescription, costOption.getAmountDescription()))
                .service(costOption.getService())
                .build();
    }
}
