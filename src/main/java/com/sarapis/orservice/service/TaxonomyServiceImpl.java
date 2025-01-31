package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.entity.Taxonomy;
import com.sarapis.orservice.exception.ResourceNotFoundException;
import com.sarapis.orservice.repository.TaxonomyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxonomyServiceImpl implements TaxonomyService {
    private final TaxonomyRepository taxonomyRepository;
    private final MetadataService metadataService;

    @Autowired
    public TaxonomyServiceImpl(TaxonomyRepository taxonomyRepository, MetadataService metadataService) {
        this.taxonomyRepository = taxonomyRepository;
        this.metadataService = metadataService;
    }

    @Override
    public List<TaxonomyDTO> getAllTaxonomies() {
        List<TaxonomyDTO> taxonomyDTOs = this.taxonomyRepository.findAll()
                .stream().map(Taxonomy::toDTO).toList();
        taxonomyDTOs.forEach(this::addRelatedData);
        return taxonomyDTOs;
    }

    @Override
    public TaxonomyDTO getTaxonomyById(String taxonomyId) {
        Taxonomy taxonomy = this.taxonomyRepository.findById(taxonomyId)
                .orElseThrow(() -> new ResourceNotFoundException("Taxonomy not found."));
        TaxonomyDTO taxonomyDTO = taxonomy.toDTO();
        this.addRelatedData(taxonomyDTO);
        return taxonomyDTO;
    }

    @Override
    public TaxonomyDTO createTaxonomy(TaxonomyDTO taxonomyDTO) {
        Taxonomy taxonomy = taxonomyDTO.toEntity();
        taxonomyDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(taxonomy.getId(), e));

        Taxonomy createdTaxonomy = this.taxonomyRepository.save(taxonomy);
        return this.getTaxonomyById(createdTaxonomy.getId());
    }

    @Override
    public TaxonomyDTO updateTaxonomy(String taxonomyId, TaxonomyDTO taxonomyDTO) {
        Taxonomy taxonomy = this.taxonomyRepository.findById(taxonomyId)
                .orElseThrow(() -> new ResourceNotFoundException("Taxonomy not found."));

        taxonomy.setName(taxonomyDTO.getName());
        taxonomy.setDescription(taxonomyDTO.getDescription());
        taxonomy.setUri(taxonomyDTO.getUri());
        taxonomy.setVersion(taxonomyDTO.getVersion());

        Taxonomy updatedTaxonomy = this.taxonomyRepository.save(taxonomy);
        return this.getTaxonomyById(updatedTaxonomy.getId());
    }

    @Override
    public void deleteTaxonomy(String taxonomyId) {
        Taxonomy taxonomy = this.taxonomyRepository.findById(taxonomyId)
                .orElseThrow(() -> new ResourceNotFoundException("Taxonomy not found."));
        this.metadataService.deleteRelatedMetadata(taxonomy.getId());
        this.taxonomyRepository.delete(taxonomy);
    }

    private void addRelatedData(TaxonomyDTO taxonomyDTO) {
        taxonomyDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(taxonomyDTO.getId()));
    }
}
