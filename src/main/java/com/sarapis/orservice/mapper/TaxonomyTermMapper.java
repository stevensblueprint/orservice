package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
import com.sarapis.orservice.model.TaxonomyTerm;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class})
public interface TaxonomyTermMapper {
  TaxonomyTerm toEntity(TaxonomyTermDTO.Request dto);
  Response toResponseDTO(TaxonomyTerm entity);
}
