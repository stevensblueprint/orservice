package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LanguageDTO;
import java.util.List;

public interface LanguageService {
  LanguageDTO.Response createLanguage(LanguageDTO.Request request);
  List<LanguageDTO.Response> getLanguagesByPhoneId(String phoneId);
  List<LanguageDTO.Response> getLanguagesByLocationId(String locationId);
}
