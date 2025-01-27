package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.CostOptionDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table(name = "cost_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostOption {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "option")
    private String option;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private int amount;

    @Column(name = "amount_description")
    private String amountDescription;

    public CostOptionDTO toDTO() {
        return CostOptionDTO.builder()
                .id(this.id)
                .serviceId(this.service.getId())
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .option(this.option)
                .currency(this.currency)
                .amount(this.amount)
                .amountDescription(this.amountDescription)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
