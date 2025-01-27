package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.LinkEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeDTO {
    private String id;

    private String linkId;

    private String linkType;
    private LinkEntity linkEntity;
    private String value;
    private String label;

    private TaxonomyTermDTO taxonomyTerm;
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Attribute toEntity(String linkId) {
        Attribute attribute = Attribute.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .linkId(linkId)
                .linkType(this.linkType)
                .linkEntity(this.linkEntity)
                .value(this.value)
                .label(this.label)
                .build();
        attribute.setTaxonomyTerm(this.taxonomyTerm.toEntity(null));
        return attribute;
    }
}
