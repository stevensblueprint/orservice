package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;

import java.util.List;

public interface AttributeService {
    List<AttributeDTO> getAllAttributes();

    List<AttributeDTO> getRelatedAttributes(String linkId);

    AttributeDTO getAttributeById(String attributeId);

    AttributeDTO createAttribute(String linkId, AttributeDTO attributeDTO);

    AttributeDTO updateAttribute(String attributeId, AttributeDTO attributeDTO);

    void deleteAttribute(String attributeId);

    void deleteRelatedAttributes(String linkId);
}
