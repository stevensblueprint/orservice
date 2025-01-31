package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.PaginationDTO;
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
    public ResponseEntity<PaginationDTO<LanguageDTO>> getAllLanguages(@RequestParam(defaultValue = "1") Integer page,
                                                                      @RequestParam(defaultValue = "10") Integer perPage) {
        List<LanguageDTO> languageDTOs = this.languageService.getAllLanguages();
        PaginationDTO<LanguageDTO> paginationDTO = PaginationDTO.of(
            languageDTOs,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDTO> getLanguageById(@PathVariable String id) {
        LanguageDTO languageDTO = this.languageService.getLanguageById(id);
        return ResponseEntity.ok(languageDTO);
    }

    @PostMapping
    public ResponseEntity<LanguageDTO> createLanguage(@RequestBody LanguageDTO languageDTO) {
        LanguageDTO createdLanguageDTO = this.languageService.createLanguage(languageDTO);
        return ResponseEntity.ok(createdLanguageDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageDTO> updateLanguage(@PathVariable String id, @RequestBody LanguageDTO languageDTO) {
        LanguageDTO updatedLanguageDTO = this.languageService.updateLanguage(id, languageDTO);
        return ResponseEntity.ok(updatedLanguageDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String id) {
        this.languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
