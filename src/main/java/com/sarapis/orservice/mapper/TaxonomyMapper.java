package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.model.Taxonomy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class, TaxonomyMapper.class})
public interface TaxonomyMapper {
  Taxonomy toEntity(TaxonomyDTO.Request dto);
  TaxonomyDTO.Response toResponseDTO(Taxonomy entity);
}
