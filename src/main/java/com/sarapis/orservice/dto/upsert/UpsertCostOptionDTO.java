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
                .validFrom(this.validFrom == null ? costOption.getValidFrom() : this.validFrom)
                .validTo(this.validTo == null ? costOption.getValidTo() : this.validTo)
                .option(this.option == null ? costOption.getOption() : this.option)
                .currency(this.currency == null ? costOption.getCurrency() : this.currency)
                .amount(this.amount == null ? costOption.getAmount() : this.amount)
                .amountDescription(this.amountDescription == null ? costOption.getAmountDescription() : this.amountDescription)
                .service(costOption.getService())
                .build();
    }
}
