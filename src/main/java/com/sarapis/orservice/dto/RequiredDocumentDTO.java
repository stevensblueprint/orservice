package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.RequiredDocument;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredDocumentDTO {
    private String id;
    private String document;
    private String uri;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public RequiredDocument toEntity() {
        return RequiredDocument.builder()
                .id(this.id)
                .document(this.document)
                .uri(this.uri)
                .build();
    }
}
