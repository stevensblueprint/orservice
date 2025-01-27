package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.entity.Language;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.repository.LanguageRepository;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final ServiceRepository serviceRepository;
    private final LocationRepository locationRepository;
    private final PhoneRepository phoneRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    public LanguageServiceImpl(LanguageRepository languageRepository,
                               ServiceRepository serviceRepository,
                               LocationRepository locationRepository,
                               PhoneRepository phoneRepository,
                               AttributeService attributeService,
                               MetadataService metadataService) {
        this.languageRepository = languageRepository;
        this.serviceRepository = serviceRepository;
        this.locationRepository = locationRepository;
        this.phoneRepository = phoneRepository;
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
        com.sarapis.orservice.entity.core.Service service = null;
        Location location = null;
        Phone phone = null;

        if (languageDTO.getServiceId() != null) {
            service = this.serviceRepository.findById(languageDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
        }
        if (languageDTO.getLocationId() != null) {
            location = this.locationRepository.findById(languageDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found."));
        }
        if (languageDTO.getPhoneId() != null) {
            phone = this.phoneRepository.findById(languageDTO.getPhoneId())
                    .orElseThrow(() -> new RuntimeException("Phone not found."));
        }

        Language language = this.languageRepository.save(languageDTO.toEntity(service, location, phone));
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
