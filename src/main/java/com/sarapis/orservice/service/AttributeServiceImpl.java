package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeServiceImpl implements AttributeService {
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public AttributeServiceImpl(AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<AttributeDTO> getAllAttributes() {
        List<AttributeDTO> attributeDTOs = this.attributeRepository.findAll().stream().map(Attribute::toDTO).toList();
        attributeDTOs.forEach(this::addRelatedData);
        return attributeDTOs;
    }

    @Override
    public AttributeDTO getAttributeById(String id) {
        Attribute attribute = this.attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found."));
        AttributeDTO attributeDTO = attribute.toDTO();
        this.addRelatedData(attributeDTO);
        return attributeDTO;
    }

    @Override
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = this.attributeRepository.save(attributeDTO.toEntity(attributeDTO.getLinkId()));

        for (MetadataDTO metadataDTO : attributeDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(attribute.getId()));
        }

        AttributeDTO savedAttributedDTO = this.attributeRepository.save(attribute).toDTO();
        this.addRelatedData(savedAttributedDTO);
        return savedAttributedDTO;
    }

    @Override
    public AttributeDTO updateAttribute(String id, AttributeDTO attributeDTO) {
        Attribute oldAttribute = this.attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found."));

        oldAttribute.setLinkId(attributeDTO.getLinkId());
        oldAttribute.setLinkType(attributeDTO.getLinkType());
        oldAttribute.setLinkEntity(attributeDTO.getLinkEntity());
        oldAttribute.setValue(attributeDTO.getValue());
        oldAttribute.setTaxonomyTerm(attributeDTO.getTaxonomyTerm().toEntity());
        oldAttribute.setLabel(attributeDTO.getLabel());

        Attribute updatedAttribute = this.attributeRepository.save(oldAttribute);
        return updatedAttribute.toDTO();
    }

    @Override
    public void deleteAttribute(String id) {
        Attribute attribute = this.attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found."));
        this.attributeRepository.deleteMetadata(attribute.getId());
        this.attributeRepository.delete(attribute);
    }

    private void addRelatedData(AttributeDTO attributeDTO) {
        attributeDTO.getMetadata().addAll(this.attributeRepository.getMetadata(attributeDTO.getId()).stream().map(Metadata::toDTO).toList());
    }
}
