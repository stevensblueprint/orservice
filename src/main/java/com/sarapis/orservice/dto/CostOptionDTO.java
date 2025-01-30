package com.sarapis.orservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Returned response for a cost option entity.
 * <a href="http://docs.openreferral.org/en/v3.1.1/hsds/schema_reference.html#cost-option">Reference</a>
 */
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
    private List<AttributeDTO> attributes;
    private List<MetadataDTO> metadata;
}
