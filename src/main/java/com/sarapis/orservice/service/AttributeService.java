package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;

import java.util.List;

public interface AttributeService {
    List<AttributeDTO> getAllAttributes();

    AttributeDTO getAttributeById(String id);

    AttributeDTO createAttribute(AttributeDTO attributeDTO);

    AttributeDTO updateAttribute(String id, AttributeDTO attributeDTO);

    void deleteAttribute(String id);
}
