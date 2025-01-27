package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.CostOption;
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
public class CostOptionDTO {
    private String id;

    private String serviceId;

    private LocalDate validFrom;
    private LocalDate validTo;
    private String option;
    private String currency;
    private int amount;
    private String amountDescription;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public CostOption toEntity(Service service) {
        return CostOption.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .service(service)
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .option(this.option)
                .currency(this.currency)
                .amount(this.amount)
                .amountDescription(this.amountDescription)
                .build();
    }
}
