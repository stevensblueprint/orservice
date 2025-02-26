package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.model.Taxonomy;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class, TaxonomyMapper.class})
public interface TaxonomyMapper {
  Taxonomy toEntity(TaxonomyDTO.Request dto);

  @Mapping(target = "taxonomyTerms", source = "taxonomyTerms")
  @Mapping(target = "metadata", source = "metadata")
  TaxonomyDTO.Response toResponseDTO(Taxonomy entity);

  List<TaxonomyDTO.Response> toResponseDTOList(List<Taxonomy> entities);

  void updateEntityFromDTo(TaxonomyDTO.UpdateRequest dto, @MappingTarget Taxonomy entity);

}
