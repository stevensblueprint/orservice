package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    private AttributeDTO mapToDTO(Attribute attribute) {
        return new AttributeDTO(
            attribute.getId(),
            attribute.getLinkId(),
            attribute.getLinkType(),
            attribute.getLinkEntity(),
            attribute.getValue(),
            attribute.getTaxonomyTerm(),
            attribute.getMetadata(),
            attribute.getLabel()
        );
    }

    private Attribute mapToEntity(AttributeDTO attributeDTO) {
        return new Attribute(
            attributeDTO.getId(),
            attributeDTO.getLinkId(),
            attributeDTO.getLinkType(),
            attributeDTO.getLinkEntity(),
            attributeDTO.getValue(),
            attributeDTO.getTaxonomyTerm(),
            attributeDTO.getMetadata(),
            attributeDTO.getLabel()
        );
    }

    @Autowired
    public AttributeServiceImpl(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public List<AttributeDTO> getAllAttributes() {
        return this.attributeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeDTO getAttributeById(String id) {
        Attribute attr = this.attributeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        return this.mapToDTO(attr);
    }

    @Override
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attr = this.mapToEntity(attributeDTO);
        Attribute savedAttr = this.attributeRepository.save(attr);
        return this.mapToDTO(savedAttr);
    }

    @Override
    public AttributeDTO updateAttribute(String id, AttributeDTO attributeDTO) {
        Attribute oldAttr = this.attributeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        oldAttr.setLinkId(attributeDTO.getLinkId());
        oldAttr.setLinkType(attributeDTO.getLinkType());
        oldAttr.setLinkEntity(attributeDTO.getLinkEntity());
        oldAttr.setValue(attributeDTO.getValue());
        oldAttr.setTaxonomyTerm(attributeDTO.getTaxonomyTerm());
        oldAttr.setMetadata(attributeDTO.getMetadata());
        oldAttr.setLabel(attributeDTO.getLabel());

        Attribute upAttr = this.attributeRepository.save(oldAttr);
        return mapToDTO(upAttr);
    }

    @Override
    public void deleteAttribute(String id) {
        Attribute target = this.attributeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        this.attributeRepository.delete(target);
    }
}
