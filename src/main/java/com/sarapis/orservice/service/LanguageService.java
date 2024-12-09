package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LanguageDTO;

import java.util.List;

public interface LanguageService {
    List<LanguageDTO> getAllLanguages();

    LanguageDTO getLanguageById(String id);

    LanguageDTO createLanguage(LanguageDTO languageDTO);

    LanguageDTO updateLanguage(String id, LanguageDTO languageDTO);

    void deleteLanguage(String id);
}
