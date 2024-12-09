package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Language;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.LanguageRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService{
    private final LanguageRepository languageRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.languageRepository = languageRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<LanguageDTO> getAllLanguages() {
        List<LanguageDTO> languageDTOs = this.languageRepository.findAll()
                .stream()
                .map(Language::toDTO)
                .toList();
        languageDTOs.forEach(this::addRelatedData);
        return languageDTOs;
    }

    @Override
    public LanguageDTO getLanguageById(String id) {
        Language language = this.languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found."));
        LanguageDTO languageDTO = language.toDTO();
        this.addRelatedData(languageDTO);
        return languageDTO;
    }

    @Override
    public LanguageDTO createLanguage(LanguageDTO languageDTO) {
        Language language = this.languageRepository.save(languageDTO.toEntity());

        for (AttributeDTO attributeDTO : languageDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(languageDTO.getId()));
        }

        for (MetadataDTO metadataDTO : languageDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(languageDTO.getId()));
        }

        LanguageDTO savedLanguageDTO = this.languageRepository.save(language).toDTO();
        this.addRelatedData(savedLanguageDTO);
        return savedLanguageDTO;
    }

    @Override
    public LanguageDTO updateLanguage(String id, LanguageDTO languageDTO) {
        Language oldLanguage = this.languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found."));

        oldLanguage.setName(languageDTO.getName());
        oldLanguage.setCode(languageDTO.getCode());
        oldLanguage.setNote(languageDTO.getNote());

        Language updatedLanguage = this.languageRepository.save(oldLanguage);
        return updatedLanguage.toDTO();
    }

    @Override
    public void deleteLanguage(String id) {
        Language language = this.languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found."));

        this.languageRepository.deleteAttributes(language.getId());
        this.languageRepository.deleteMetadata(language.getId());
        this.languageRepository.delete(language);
    }

    private void addRelatedData(LanguageDTO languageDTO) {
        List<AttributeDTO> attributes = this.languageRepository.getAttributes(languageDTO.getId())
                .stream()
                .map(Attribute::toDTO)
                .toList();

        List<MetadataDTO> metadata = this.languageRepository.getMetadata(languageDTO.getId())
                .stream()
                .map(Metadata::toDTO)
                .toList();

        languageDTO.getAttributes().addAll(attributes);
        languageDTO.getMetadata().addAll(metadata);
    }
}
