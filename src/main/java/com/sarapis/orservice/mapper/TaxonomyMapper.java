package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.TAXONOMY_RESOURCE_TYPE;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.model.Taxonomy;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaxonomyMapper {
  Taxonomy toEntity(TaxonomyDTO.Request dto);
  TaxonomyDTO.Response toResponseDTO(Taxonomy entity);

  default  TaxonomyDTO.Response toResponseDTO(Taxonomy entity, MetadataService metadataService) {
    TaxonomyDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(Taxonomy taxonomy, TaxonomyDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            taxonomy.getId(), TAXONOMY_RESOURCE_TYPE
        )
    );
  }
}
