package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Funding;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingDTO {
    private String id;

    private String organizationId;
    private String serviceId;

    private String source;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Funding toEntity(Organization organization, Service service) {
        return Funding.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .organization(organization)
                .service(service)
                .source(this.source)
                .build();
    }
}
