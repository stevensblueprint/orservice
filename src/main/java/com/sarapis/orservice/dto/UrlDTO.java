package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Url;
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
public class UrlDTO {
    private String id;

    private String organizationId;
    private String serviceId;

    private String label;
    private String url;
    
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Url toEntity(Organization organization, Service service) {
        return Url.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .organization(organization)
                .service(service)
                .label(this.label)
                .url(this.url)
                .build();
    }
}
