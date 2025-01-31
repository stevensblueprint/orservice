package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.TaxonomyTerm;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxonomyTermDTO {
    private String id;

    private String parentId;

    private String code;
    private String name;
    private String description;
    private String taxonomy;
    private String language;
    private String termUri;

    private TaxonomyDTO taxonomyDetail;
    private List<MetadataDTO> metadata = new ArrayList<>();

    public TaxonomyTerm toEntity(TaxonomyTerm parent) {
        TaxonomyTerm taxonomyTerm = TaxonomyTerm.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .parent(parent)
                .code(this.code)
                .name(this.name)
                .description(this.description)
                .taxonomy(this.taxonomy)
                .language(this.language)
                .termUri(this.termUri)
                .build();
        taxonomyTerm.setTaxonomyDetail(this.taxonomyDetail == null ? null : this.taxonomyDetail.toEntity());
        return taxonomyTerm;
    }
}
