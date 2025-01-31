package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.entity.TaxonomyTerm;
import com.sarapis.orservice.exception.ResourceNotFoundException;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyTermServiceImpl implements TaxonomyTermService {
    private final TaxonomyTermRepository taxonomyTermRepository;
    private final MetadataService metadataService;

    @Autowired
    public TaxonomyTermServiceImpl(TaxonomyTermRepository taxonomyTermRepository, MetadataService metadataService) {
        this.taxonomyTermRepository = taxonomyTermRepository;
        this.metadataService = metadataService;

    }

    @Override
    public List<TaxonomyTermDTO> getAllTaxonomyTerms() {
        List<TaxonomyTermDTO> taxonomyTermDTOs = this.taxonomyTermRepository.findAll()
                .stream().map(TaxonomyTerm::toDTO).toList();
        taxonomyTermDTOs.forEach(this::addRelatedData);
        return taxonomyTermDTOs;
    }

    @Override
    public TaxonomyTermDTO getTaxonomyTermById(String taxonomyTermId) {
        TaxonomyTerm taxonomyTerm = this.taxonomyTermRepository.findById(taxonomyTermId)
                .orElseThrow(() -> new ResourceNotFoundException("Taxonomy term not found."));
        TaxonomyTermDTO taxonomyTermDTO = taxonomyTerm.toDTO();
        this.addRelatedData(taxonomyTermDTO);
        return taxonomyTermDTO;
    }

    @Override
    public TaxonomyTermDTO createTaxonomyTerm(TaxonomyTermDTO taxonomyTermDTO) {
        TaxonomyTerm parent = null;

        if (taxonomyTermDTO.getParentId() != null) {
            parent = this.taxonomyTermRepository.findById(taxonomyTermDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Taxonomy term not found."));
        }

        TaxonomyTerm taxonomyTerm = taxonomyTermDTO.toEntity(parent);
        taxonomyTermDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(taxonomyTerm.getId(), e));

        TaxonomyTerm createdTaxonomyTerm = this.taxonomyTermRepository.save(taxonomyTerm);
        return this.getTaxonomyTermById(createdTaxonomyTerm.getId());
    }

    @Override
    public TaxonomyTermDTO updateTaxonomyTerm(String taxonomyTermId, TaxonomyTermDTO taxonomyTermDTO) {
        TaxonomyTerm taxonomyTerm = this.taxonomyTermRepository.findById(taxonomyTermId)
                .orElseThrow(() -> new ResourceNotFoundException("Taxonomy term not found."));

        taxonomyTerm.setCode(taxonomyTermDTO.getCode());
        taxonomyTerm.setName(taxonomyTermDTO.getName());
        taxonomyTerm.setDescription(taxonomyTermDTO.getDescription());
        taxonomyTerm.setTaxonomy(taxonomyTermDTO.getTaxonomy());
        taxonomyTerm.setLanguage(taxonomyTermDTO.getLanguage());
        taxonomyTerm.setTermUri(taxonomyTermDTO.getTermUri());

        TaxonomyTerm updatedTaxonomyTerm = this.taxonomyTermRepository.save(taxonomyTerm);
        return this.getTaxonomyTermById(updatedTaxonomyTerm.getId());
    }

    @Override
    public void deleteTaxonomyTerm(String taxonomyTermId) {
        TaxonomyTerm taxonomyTerm = this.taxonomyTermRepository.findById(taxonomyTermId)
                .orElseThrow(() -> new ResourceNotFoundException("Taxonomy term not found."));
        this.metadataService.deleteRelatedMetadata(taxonomyTerm.getId());
        this.taxonomyTermRepository.delete(taxonomyTerm);
    }

    private void addRelatedData(TaxonomyTermDTO taxonomyTermDTO) {
        taxonomyTermDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(taxonomyTermDTO.getId()));
    }
}
