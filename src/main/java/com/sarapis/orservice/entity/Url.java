package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "label")
    private String label;

    @Column(name = "url", nullable = false)
    private String url;

    public UrlDTO toDTO() {
        return UrlDTO.builder()
                .id(this.id)
                .organizationId(this.organization == null ? null : this.organization.getId())
                .serviceId(this.service == null ? null : this.service.getId())
                .label(this.label)
                .url(this.url)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
