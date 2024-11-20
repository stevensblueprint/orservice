package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.TaxonomyTerm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDTO {
    private String id;
    private String linkId;
    private String linkType;
    private String linkEntity;
    private String value;
    private TaxonomyTerm taxonomyTerm;
    private Metadata metadata;
    private String label;
}
