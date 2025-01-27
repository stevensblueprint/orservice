package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeServiceImpl implements AttributeService {
    private final AttributeRepository attributeRepository;
    private final MetadataService metadataService;

    @Autowired
    public AttributeServiceImpl(AttributeRepository attributeRepository,
                                MetadataService metadataService) {
        this.attributeRepository = attributeRepository;
        this.metadataService = metadataService;
    }

    @Override
    public List<AttributeDTO> getAllAttributes() {
        List<AttributeDTO> attributeDTOs = this.attributeRepository.findAll()
                .stream().map(Attribute::toDTO).toList();
        attributeDTOs.forEach(this::addRelatedData);
        return attributeDTOs;
    }

    @Override
    public List<AttributeDTO> getRelatedAttributes(String linkId) {
        return this.attributeRepository.getRelatedAttributes(linkId)
                .stream().map(Attribute::toDTO).toList();
    }

    @Override
    public AttributeDTO getAttributeById(String attributeId) {
        Attribute attribute = this.attributeRepository.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Attribute not found."));
        AttributeDTO attributeDTO = attribute.toDTO();
        this.addRelatedData(attributeDTO);
        return attributeDTO;
    }

    @Override
    public AttributeDTO createAttribute(String linkId, AttributeDTO attributeDTO) {
        Attribute attribute = this.attributeRepository.save(attributeDTO.toEntity(linkId));
        attributeDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(attribute.getId(), e));

        Attribute createdAttribute = this.attributeRepository.save(attribute);
        return this.getAttributeById(createdAttribute.getId());
    }

    @Override
    public AttributeDTO updateAttribute(String attributeId, AttributeDTO attributeDTO) {
        Attribute attribute = this.attributeRepository.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Attribute not found."));

        attribute.setLinkId(attributeDTO.getLinkId());
        attribute.setLinkType(attributeDTO.getLinkType());
        attribute.setLinkEntity(attributeDTO.getLinkEntity());
        attribute.setValue(attributeDTO.getValue());
        attribute.setLabel(attributeDTO.getLabel());
        attribute.setTaxonomyTerm(attributeDTO.getTaxonomyTerm().toEntity(null));

        Attribute updatedAttribute = this.attributeRepository.save(attribute);
        return this.getAttributeById(updatedAttribute.getId());
    }

    @Override
    public void deleteAttribute(String attributeId) {
        Attribute attribute = this.attributeRepository.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Attribute not found."));
        this.metadataService.deleteRelatedMetadata(attribute.getId());
        this.attributeRepository.delete(attribute);
    }

    @Override
    public void deleteRelatedAttributes(String linkId) {
        this.attributeRepository.deleteRelatedAttributes(linkId);
    }

    private void addRelatedData(AttributeDTO attributeDTO) {
        attributeDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(attributeDTO.getId()));
    }
}
