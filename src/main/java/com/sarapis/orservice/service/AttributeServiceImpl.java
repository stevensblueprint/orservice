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
import java.util.Objects;

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
        List<AttributeDTO> attributeDTOs = this.attributeRepository.findAll()
                .stream()
                .map(Attribute::toDTO)
                .toList();
        attributeDTOs.forEach(this::getRelatedData);

        return attributeDTOs;
    }

    @Override
    public AttributeDTO getAttributeById(String id) {
        Attribute attribute = this.attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found."));
        AttributeDTO attributeDTO = attribute.toDTO();
        this.getRelatedData(attributeDTO);
        return attributeDTO;
    }

    @Override
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = this.attributeRepository.save(attributeDTO.toEntity(attributeDTO.getLinkId()));

        for (MetadataDTO metadataDTO : attributeDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(attribute.getId()));
        }

        Attribute savedAttr = this.attributeRepository.save(attribute);
        getRelatedData(attributeDTO);
        return savedAttr.toDTO();
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

        List<Metadata> metadatas = metadataRepository.findAll();
        List<Metadata> relatedMetadatas = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), attribute.getId())).toList();
        this.metadataRepository.deleteAll(relatedMetadatas);

        this.attributeRepository.delete(attribute);
    }

    private void getRelatedData(AttributeDTO attributeDTO) {
        List<Metadata> metadatas = this.metadataRepository.findAll();
        List<MetadataDTO> relatedMetadataDTOs = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), attributeDTO.getId())).map(Metadata::toDTO).toList();

        attributeDTO.getMetadata().addAll(relatedMetadataDTOs);
    }
}
