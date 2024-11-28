package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    @Autowired
    public AttributeServiceImpl(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public List<AttributeDTO> getAllAttributes() {
        return this.attributeRepository.findAll()
                .stream()
                .map(Attribute::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeDTO getAttributeById(String id) {
        Attribute attr = this.attributeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        return attr.toDTO();
    }

    @Override
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attr = attributeDTO.toEntity();
        Attribute savedAttr = this.attributeRepository.save(attr);
        return savedAttr.toDTO();
    }

    @Override
    public AttributeDTO updateAttribute(String id, AttributeDTO attributeDTO) {
        Attribute oldAttr = this.attributeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        //oldAttr.setLinkType(attributeDTO.getLinkType());
        oldAttr.setLinkEntity(attributeDTO.getLinkEntity());
        oldAttr.setValue(attributeDTO.getValue());
        oldAttr.setTaxonomyTerm(attributeDTO.getTaxonomyTerm().toEntity());

        oldAttr.setMetadata(attributeDTO.getMetadata().stream()
                .map(MetadataDTO::toEntity)
                .collect(Collectors.toList())
        );
        oldAttr.setLabel(attributeDTO.getLabel());

        Attribute upAttr = this.attributeRepository.save(oldAttr);
        return upAttr.toDTO();
    }

    @Override
    public void deleteAttribute(String id) {
        Attribute target = this.attributeRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        this.attributeRepository.delete(target);
    }
}
