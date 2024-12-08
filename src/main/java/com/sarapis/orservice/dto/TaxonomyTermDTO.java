package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.TaxonomyTerm;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxonomyTermDTO {
  private String id;
  private String code;
  private String name;
  private String description;
  private TaxonomyTermDTO parent = null;
  private String taxonomy;
  private TaxonomyDTO taxonomyDetail = null;
  private String language;
  private String termUri;
  private List<MetadataDTO> metadata = new ArrayList<>();

  public TaxonomyTerm toEntity() {
    return TaxonomyTerm.builder()
            .id(this.id)
            .code(this.code)
            .name(this.name)
            .description(this.description)
            .parent(this.parent != null ? this.parent.toEntity() : null)
            .taxonomy(this.taxonomy)
            .taxonomyDetail(this.taxonomyDetail != null ? this.taxonomyDetail.toEntity() : null)
            .language(this.language)
            .termUri(this.termUri)
            .build();
  }
}
