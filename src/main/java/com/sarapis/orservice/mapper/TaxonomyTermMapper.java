package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.TAXONOMY_TERM_RESOURCE_TYPE;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
import com.sarapis.orservice.model.TaxonomyTerm;
import com.sarapis.orservice.repository.TaxonomyRepository;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TaxonomyTermMapper {

  @Autowired
  private TaxonomyTermRepository taxonomyTermRepository;

  @Autowired
  private TaxonomyRepository taxonomyRepository;

  public abstract TaxonomyTerm toEntity(TaxonomyTermDTO.Request dto);
  public abstract Response toResponseDTO(TaxonomyTerm entity);

  @AfterMapping
  public TaxonomyTerm toEntity(@MappingTarget TaxonomyTerm taxonomyTerm) {
    if (taxonomyTerm.getParent().getId() != null) {
      taxonomyTerm.setParent(
          taxonomyTermRepository.findById(taxonomyTerm.getParent().getId()).orElseThrow(
              () -> new IllegalArgumentException("Parent taxonomy term not found for taxonomy term with ID: " + taxonomyTerm.getId())
          )
      );
    }

    if (taxonomyTerm.getTaxonomyDetail().getId() != null) {
      taxonomyTerm.setTaxonomyDetail(
          taxonomyRepository.findById(taxonomyTerm.getTaxonomyDetail().getId()).orElseThrow(
              () -> new IllegalArgumentException("Taxonomy not found for taxonomy term with ID: " + taxonomyTerm.getId())
          )
      );
    }

    return taxonomyTerm;
  }

  public TaxonomyTermDTO.Response toResponseDTO(TaxonomyTerm entity, MetadataService metadataService) {
    TaxonomyTermDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  private void enrichMetadata(TaxonomyTerm taxonomyTerm, TaxonomyTermDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            taxonomyTerm.getId(), TAXONOMY_TERM_RESOURCE_TYPE
        )
    );
  }
}
