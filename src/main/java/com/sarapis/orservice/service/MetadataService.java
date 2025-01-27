package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.MetadataDTO;

import java.util.List;

public interface MetadataService {
    List<MetadataDTO> getAllMetadata();

    List<MetadataDTO> getRelatedMetadata(String resourceId);

    MetadataDTO getMetadata(String metadataId);

    MetadataDTO createMetadata(String resourceId, MetadataDTO metadataDTO);

    MetadataDTO updateMetadata(String metadataId, String resourceId, MetadataDTO metadataDTO);

    void deleteMetadata(String metadataId);

    void deleteRelatedMetadata(String resourceId);
}
