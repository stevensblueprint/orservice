package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.dto.TaxonomyDTO.Request;
import com.sarapis.orservice.dto.TaxonomyDTO.Response;
import com.sarapis.orservice.mapper.TaxonomyMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Taxonomy;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.TaxonomyRepository;
import com.sarapis.orservice.repository.TaxonomySpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import static com.sarapis.orservice.utils.FieldMap.TAXONOMY_FIELD_MAP;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaxonomyServiceImpl implements  TaxonomyService {

  private final TaxonomyRepository taxonomyRepository;
  private final TaxonomyMapper taxonomyMapper;
  private final MetadataRepository metadataRepository;
  private final MetadataService metadataService;

  private static final int RECORDS_PER_STREAM = 100;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllTaxonomies(String search, Integer page, Integer perPage) {
    Specification<Taxonomy> spec = buildSpecification(search);
    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Taxonomy> taxonomyPage = taxonomyRepository.findAll(spec, pageable);
    Page<TaxonomyDTO.Response> dtoPage = taxonomyPage.map(taxonomy -> taxonomyMapper.toResponseDTO(taxonomy, metadataService));
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public void streamAllTaxonomies(String search, Consumer<Response> consumer) {
    Specification<Taxonomy> spec = buildSpecification(search);
    int currentPage = 0;
    boolean hasMoreData = true;

    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<Taxonomy> taxonomyPage = taxonomyRepository.findAll(spec, pageable);

      List<Taxonomy> taxonomies = taxonomyPage.getContent();

      if (taxonomies.isEmpty()) {
        hasMoreData = false;
      } else {
        for (Taxonomy taxonomy : taxonomies) {
          consumer.accept(taxonomyMapper.toResponseDTO(taxonomy, metadataService));
        }
        currentPage++;
      }
    }
  }


  @Override
  @Transactional(readOnly = true)
  public Response getTaxonomyById(String id) {
    Taxonomy taxonomy = taxonomyRepository.findById(id).orElseThrow();
    return taxonomyMapper.toResponseDTO(taxonomy, metadataService);
  }

  @Override
  @Transactional
  public Response createTaxonomy(Request requestDto, String updatedBy) {
    if (requestDto.getId() == null || StringUtils.isBlank(requestDto.getId())) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Taxonomy taxonomy = taxonomyMapper.toEntity(requestDto);
    taxonomy.setMetadata(metadataRepository, updatedBy);
    taxonomy = taxonomyRepository.save(taxonomy);
    return taxonomyMapper.toResponseDTO(taxonomy, metadataService);
  }

  @Override
  @Transactional
  public Response undoTaxonomyMetadata(String metadataId) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new RuntimeException("Metadata not found"));

    Taxonomy reverted = MetadataUtils.undoMetadata(
            metadata,
            this.metadataRepository,
            this.taxonomyRepository,
            TAXONOMY_FIELD_MAP
    );
    return taxonomyMapper.toResponseDTO(reverted, metadataService);
  }

  private Specification<Taxonomy> buildSpecification(String search) {
    Specification<Taxonomy> spec = Specification.where(null);

    if (search!= null &&!search.isEmpty()) {
      spec = spec.and(TaxonomySpecifications.hasSearchTerm(search));
    }
    return spec;
  }
}
