package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "funding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funding {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "source")
    private String source;

    public FundingDTO toDTO() {
        return FundingDTO.builder()
                .id(this.id)
                .organizationId(this.organization == null ? null : this.organization.getId())
                .serviceId(this.service == null ? null : this.service.getId())
                .source(this.source)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
