package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.model.TaxonomyTerm;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class})
public interface TaxonomyTermMapper {
  TaxonomyTerm toEntity(TaxonomyTermDTO.Request dto);

  @Mapping(target = "parent", source = "parent")
  @Mapping(target = "children", source = "children")
  @Mapping(target = "metadata", source = "metadata")
  TaxonomyTermDTO.Response toResponseDTO(TaxonomyTerm entity);

  List<TaxonomyTermDTO.Response> toResponseDTOList(List<TaxonomyTerm> entities);

  void updateEntityFromDto(TaxonomyTermDTO.UpdateRequest dto, @MappingTarget TaxonomyTerm entity);
}
