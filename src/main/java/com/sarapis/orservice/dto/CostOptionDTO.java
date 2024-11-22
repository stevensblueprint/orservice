package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.CostOption;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostOptionDTO {
    private String id;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String option;
    private String currency;
    private int amount;
    private String amountDescription;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public CostOption toEntity() {
        return CostOption.builder()
                .id(this.id)
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .option(this.option)
                .currency(this.currency)
                .amount(this.amount)
                .amountDescription(this.amountDescription)
                .attributes(this.attributes.stream().map(AttributeDTO::toEntity).toList())
                .metadata(this.metadata.stream().map(MetadataDTO::toEntity).toList())
                .build();
    }
}
