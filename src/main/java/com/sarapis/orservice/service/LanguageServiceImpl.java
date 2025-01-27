package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.entity.Language;
import com.sarapis.orservice.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    public LanguageServiceImpl(LanguageRepository languageRepository,
                               AttributeService attributeService,
                               MetadataService metadataService) {
        this.languageRepository = languageRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<LanguageDTO> getAllLanguages() {
        List<LanguageDTO> languageDTOs = this.languageRepository.findAll().stream().map(Language::toDTO).toList();
        languageDTOs.forEach(this::addRelatedData);
        return languageDTOs;
    }

    @Override
    public LanguageDTO getLanguageById(String languageId) {
        Language language = this.languageRepository.findById(languageId)
                .orElseThrow(() -> new RuntimeException("Language not found."));
        LanguageDTO languageDTO = language.toDTO();
        this.addRelatedData(languageDTO);
        return languageDTO;
    }

    @Override
    public LanguageDTO createLanguage(LanguageDTO languageDTO) {
        Language language = this.languageRepository.save(languageDTO.toEntity(null, null, null));
        languageDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(language.getId(), attributeDTO));
        languageDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(language.getId(), e));

        Language createdLanguage = this.languageRepository.save(language);
        return this.getLanguageById(createdLanguage.getId());
    }

    @Override
    public LanguageDTO updateLanguage(String languageId, LanguageDTO languageDTO) {
        Language language = this.languageRepository.findById(languageId)
                .orElseThrow(() -> new RuntimeException("Language not found."));

        language.setName(languageDTO.getName());
        language.setCode(languageDTO.getCode());
        language.setNote(languageDTO.getNote());

        Language updatedLanguage = this.languageRepository.save(language);
        return this.getLanguageById(updatedLanguage.getId());
    }

    @Override
    public void deleteLanguage(String languageId) {
        Language language = this.languageRepository.findById(languageId)
                .orElseThrow(() -> new RuntimeException("Language not found."));
        this.attributeService.deleteRelatedAttributes(language.getId());
        this.metadataService.deleteRelatedMetadata(language.getId());
        this.languageRepository.delete(language);
    }

    private void addRelatedData(LanguageDTO languageDTO) {
        languageDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(languageDTO.getId()));
        languageDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(languageDTO.getId()));
    }
}
