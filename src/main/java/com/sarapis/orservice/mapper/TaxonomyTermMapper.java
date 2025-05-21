package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.TAXONOMY_TERM_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
import com.sarapis.orservice.model.TaxonomyTerm;
import com.sarapis.orservice.repository.TaxonomyRepository;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class TaxonomyTermMapper {

  @Autowired
  private TaxonomyTermRepository taxonomyTermRepository;

  @Autowired
  private TaxonomyRepository taxonomyRepository;

  @Autowired
  private TaxonomyMapper taxonomyMapper;

  @Mapping(target = "parent.id", source = "parentId")
  @Mapping(target = "taxonomyDetail", source = "taxonomyDetail")
  public abstract TaxonomyTerm toEntity(TaxonomyTermDTO.Request dto);

  @Mapping(target = "parentId", source = "parent.id")
  public abstract Response toResponseDTO(TaxonomyTerm entity);

  @AfterMapping
  public void afterMappingToEntity(TaxonomyTermDTO.Request dto, @MappingTarget TaxonomyTerm taxonomyTerm) {
    if (taxonomyTerm.getId() == null) {
      if (dto.getId() != null && !dto.getId().isEmpty()) {
        taxonomyTerm.setId(dto.getId());
      } else {
        taxonomyTerm.setId(UUID.randomUUID().toString());
      }
    }

    if (dto.getParentId() != null && !dto.getParentId().isEmpty()) {
      taxonomyTerm.setParent(
          taxonomyTermRepository.findById(dto.getParentId()).orElseThrow(
              () -> new IllegalArgumentException("Parent taxonomy term not found with ID: " + dto.getParentId())
          )
      );
    }

    if (dto.getParentId() == null) {
      taxonomyTerm.setParent(null);
    }

    if (dto.getTaxonomyDetail() != null) {
      if (dto.getTaxonomyDetail().getId() != null) {
        taxonomyTerm.setTaxonomyDetail(
            taxonomyRepository.findById(dto.getTaxonomyDetail().getId()).orElseThrow(
                () -> new IllegalArgumentException("Taxonomy not found with ID: " + dto.getTaxonomyDetail().getId())
            )
        );
      } else if (taxonomyTerm.getTaxonomyDetail() != null) {
        if (taxonomyTerm.getTaxonomyDetail().getId() == null) {
          taxonomyTerm.getTaxonomyDetail().setId(UUID.randomUUID().toString());
        }
      }
    }
  }

  public TaxonomyTermDTO.Response toResponseDTO(TaxonomyTerm entity, MetadataService metadataService) {
    TaxonomyTermDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        TaxonomyTerm::getId,
        TaxonomyTermDTO.Response::setMetadata,
        TAXONOMY_TERM_RESOURCE_TYPE,
        metadataService
    );

    if (entity.getTaxonomyDetail()!= null) {
      TaxonomyDTO.Response enrichedTaxonomyDetail =
          taxonomyMapper.toResponseDTO(entity.getTaxonomyDetail(), metadataService);
      response.setTaxonomyDetail(enrichedTaxonomyDetail);
    }
    return response;
  }
}