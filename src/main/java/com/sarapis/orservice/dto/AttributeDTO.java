package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.LinkEntity;
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
    private LinkEntity linkEntity;
    private String value;
    private TaxonomyTermDTO taxonomyTerm;
    private String label;
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Attribute toEntity(String linkId) {
        return Attribute.builder()
                .id(this.id)
                .linkId(linkId)
                .linkEntity(this.linkEntity)
                .value(this.value)
                .taxonomyTerm(this.taxonomyTerm.toEntity())
                .label(this.label)
                .build();
    }
}
