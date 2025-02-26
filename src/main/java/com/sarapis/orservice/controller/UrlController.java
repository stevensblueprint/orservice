package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.service.UrlService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
@Slf4j
public class UrlController {
  private final UrlService urlService;

  @GetMapping
  public ResponseEntity<PaginationDTO<UrlDTO.Response>> getAllUrls(
      @RequestParam(name = "page", defaultValue = "1")
      @Min(value = 1, message = "Invalid input provided for 'page'.")
      int page,
      @RequestParam(name = "perPage", defaultValue = "10")
      @Min(value = 1, message = "Invalid input provided for 'perPage'.")
      int perPage
  ) {
    log.info("Received request to get all URLs");
    List<UrlDTO.Response> urls = urlService.getAllUrls();
    PaginationDTO<UrlDTO.Response> paginationDTO = PaginationDTO.of(urls, page, perPage);
    log.info("Returning {} URLs", urls.size());
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UrlDTO.Response> getUrlById(@PathVariable String id) {
    log.info("Received request to get URL by id: {}", id);
    UrlDTO.Response url = urlService.getUrlById(id);
    log.info("Returning URL with id: {}", id);
    return ResponseEntity.ok(url);
  }

  @PostMapping
  public ResponseEntity<UrlDTO.Response> createUrl(@Valid @RequestBody UrlDTO.Request requestDto) {
    log.info("Received request to create URL with data: {}", requestDto);
    UrlDTO.Response createdUrl = urlService.createUrl(requestDto);
    log.info("Created URL with id: {}", createdUrl.getId());
    return new ResponseEntity<>(createdUrl, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UrlDTO.Response> updateUrl(
      @PathVariable String id,
      @Valid @RequestBody UrlDTO.UpdateRequest updateDto,
      @RequestHeader("X-Updated-By") String updatedBy) {
    log.info("Received request to update URL with id: {} by user: {}", id, updatedBy);
    UrlDTO.Response updatedUrl = urlService.updateUrl(id, updateDto, updatedBy);
    log.info("Updated URL with id: {}", id);
    return ResponseEntity.ok(updatedUrl);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUrl(
      @PathVariable String id,
      @RequestHeader("X-Deleted-By") String deletedBy) {
    log.info("Received request to delete URL with id: {} by user: {}", id, deletedBy);
    urlService.deleteUrl(id, deletedBy);
    log.info("Deleted URL with id: {}", id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/organization/{organizationId}")
  public ResponseEntity<List<UrlDTO.Response>> getUrlsByOrganizationId(@PathVariable String organizationId) {
    log.info("Received request to get URLs for organization id: {}", organizationId);
    List<UrlDTO.Response> urls = urlService.getUrlsByOrganizationId(organizationId);
    log.info("Returning {} URLs for organization id: {}", urls.size(), organizationId);
    return ResponseEntity.ok(urls);
  }

  @GetMapping("/service/{serviceId}")
  public ResponseEntity<List<UrlDTO.Response>> getUrlsByServiceId(@PathVariable String serviceId) {
    log.info("Received request to get URLs for service id: {}", serviceId);
    List<UrlDTO.Response> urls = urlService.getUrlsByServiceId(serviceId);
    log.info("Returning {} URLs for service id: {}", urls.size(), serviceId);
    return ResponseEntity.ok(urls);
  }
}
