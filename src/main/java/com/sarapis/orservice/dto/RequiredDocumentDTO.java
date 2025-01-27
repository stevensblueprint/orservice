package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.RequiredDocument;
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
public class RequiredDocumentDTO {
    private String id;

    private String serviceId;

    private String document;
    private String uri;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public RequiredDocument toEntity(Service service) {
        return RequiredDocument.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .service(service)
                .document(this.document)
                .uri(this.uri)
                .build();
    }
}
