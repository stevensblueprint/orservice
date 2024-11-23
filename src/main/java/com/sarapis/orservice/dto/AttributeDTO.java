package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Attribute;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeDTO {
    private String id;
    private String linkEntity;
    private String value;
    private TaxonomyTermDTO taxonomyTerm = null;
    private List<MetadataDTO> metadata = new ArrayList<>();
    private String label;

    public Attribute toEntity() {
        return Attribute.builder()
                .id(this.id)
                .linkEntity(this.linkEntity)
                .value(this.value)
                .taxonomyTerm(this.taxonomyTerm != null ? this.taxonomyTerm.toEntity() : null)
                .metadata(this.metadata.stream().map(MetadataDTO::toEntity).toList())
                .label(this.label)
                .build();
    }
}
