package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.OrganizationIdentifier;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationIdentifierDTO {
    private String id;
    private String identifierScheme;
    private String identifierType;
    private String identifier;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public OrganizationIdentifier toEntity() {
        return OrganizationIdentifier.builder()
                .id(this.id)
                .identifierScheme(this.identifierScheme)
                .identifierType(this.identifierType)
                .identifier(this.identifier)
                .attributes(this.attributes.stream().map(AttributeDTO::toEntity).toList())
                .metadata(this.metadata.stream().map(MetadataDTO::toEntity).toList())
                .build();
    }
}
