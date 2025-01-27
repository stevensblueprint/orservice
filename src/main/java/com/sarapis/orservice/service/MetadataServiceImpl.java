package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataServiceImpl implements MetadataService {
    private final MetadataRepository metadataRepository;

    @Autowired
    public MetadataServiceImpl(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<MetadataDTO> getAllMetadata() {
        return this.metadataRepository.findAll()
                .stream().map(Metadata::toDTO).toList();
    }

    @Override
    public List<MetadataDTO> getRelatedMetadata(String resourceId) {
        return this.metadataRepository.getRelatedMetadata(resourceId)
                .stream().map(Metadata::toDTO).toList();
    }

    @Override
    public MetadataDTO getMetadata(String metadataId) {
        Metadata metadata = this.metadataRepository.findById(metadataId)
                .orElseThrow(() -> new RuntimeException("Metadata not found."));
        return metadata.toDTO();
    }

    @Override
    public MetadataDTO createMetadata(String resourceId, MetadataDTO metadataDTO) {
        return this.metadataRepository.save(metadataDTO.toEntity(resourceId)).toDTO();
    }

    @Override
    public MetadataDTO updateMetadata(String metadataId, String resourceId, MetadataDTO metadataDTO) {
        Metadata metadata = this.metadataRepository.findById(metadataId)
                .orElseThrow(() -> new RuntimeException("Metadata not found."));

        metadata.setResourceId(resourceId);
        metadata.setResourceType(metadataDTO.getResourceType());
        metadata.setLastActionDate(metadataDTO.getLastActionDate());
        metadata.setLastActionType(metadataDTO.getLastActionType());
        metadata.setFieldName(metadataDTO.getFieldName());
        metadata.setPreviousValue(metadataDTO.getPreviousValue());
        metadata.setReplacementValue(metadataDTO.getReplacementValue());
        metadata.setUpdatedBy(metadataDTO.getUpdatedBy());

        return this.metadataRepository.save(metadataDTO.toEntity(metadataId)).toDTO();
    }

    @Override
    public void deleteMetadata(String metadataId) {
        Metadata metadata = this.metadataRepository.findById(metadataId)
                .orElseThrow(() -> new RuntimeException("Metadata not found."));
        this.metadataRepository.delete(metadata);
    }

    @Override
    public void deleteRelatedMetadata(String resourceId) {
        this.metadataRepository.deleteById(resourceId);
    }
}
