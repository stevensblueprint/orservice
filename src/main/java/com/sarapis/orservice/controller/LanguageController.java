package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mapping")
public class LanguageController {
    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<List<LanguageDTO>> getAllLanguages() {
        List<LanguageDTO> languages = this.languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/{languageId}")
    public ResponseEntity<LanguageDTO> getLanguageById(@PathVariable String languageId) {
        LanguageDTO language = this.languageService.getLanguageById(languageId);
        return ResponseEntity.ok(language);
    }

    @PostMapping
    public ResponseEntity<LanguageDTO> createLanguage(@RequestBody LanguageDTO languageDTO) {
        LanguageDTO createdLanguage = this.languageService.createLanguage(languageDTO);
        return ResponseEntity.ok(createdLanguage);
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageDTO> updateLanguage(@PathVariable String languageId,
                                                      @RequestBody LanguageDTO languageDTO) {
        LanguageDTO updatedLanguage = this.languageService.updateLanguage(languageId, languageDTO);
        return ResponseEntity.ok(updatedLanguage);
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String languageId) {
        this.languageService.deleteLanguage(languageId);
        return ResponseEntity.noContent().build();
    }
}
