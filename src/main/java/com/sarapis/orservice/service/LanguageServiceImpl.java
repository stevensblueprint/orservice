package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.LANGUAGE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.LanguageDTO.Request;
import com.sarapis.orservice.dto.LanguageDTO.Response;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.mapper.LanguageMapper;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.repository.LanguageRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

  private final LanguageRepository languageRepository;
  private final LanguageMapper languageMapper;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createLanguage(Request request) {
    if (request.getId() == null || request.getId().trim().isEmpty()) {
      request.setId(UUID.randomUUID().toString());
    }
    Language language = languageMapper.toEntity(request);
    Language savedLanguage = languageRepository.save(language);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedLanguage.getId(),
        LANGUAGE_RESOURCE_TYPE,
        CREATE.name(),
        "language",
        EMPTY_PREVIOUS_VALUE,
        languageMapper.toResponseDTO(savedLanguage).toString(),
        "SYSTEM"
    );
    LanguageDTO.Response response = languageMapper.toResponseDTO(language);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedLanguage.getId(), LANGUAGE_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getLanguagesByPhoneId(String phoneId) {
    List<Language> languages = languageRepository.findByPhoneId(phoneId);
    List<LanguageDTO.Response> languageDtos = languages.stream().map(languageMapper::toResponseDTO).toList();
    languageDtos = languageDtos.stream().peek(language -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          language.getId(), LANGUAGE_RESOURCE_TYPE
      );
      language.setMetadata(metadata);
    }).toList();
    return languageDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getLanguagesByLocationId(String locationId) {
    List<Language> languages = languageRepository.findByLocationId(locationId);
    List<LanguageDTO.Response> languageDtos = languages.stream().map(languageMapper::toResponseDTO).toList();
    languageDtos = languageDtos.stream().peek(language -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          language.getId(), LANGUAGE_RESOURCE_TYPE
      );
      language.setMetadata(metadata);
    }).toList();
    return languageDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getLanguagesByServiceId(String serviceId) {
    List<Language> languages = languageRepository.findByServiceId(serviceId);
    List<LanguageDTO.Response> languageDtos = languages.stream().map(languageMapper::toResponseDTO).toList();
    languageDtos = languageDtos.stream().peek(language -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          language.getId(), LANGUAGE_RESOURCE_TYPE
      );
      language.setMetadata(metadata);
    }).toList();
    return languageDtos;
  }
}
