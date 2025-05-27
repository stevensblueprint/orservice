package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.LinkTypeDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.LinkTypeService;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link_types")
@RequiredArgsConstructor
@Slf4j
public class LinkTypeController {
  private final LinkTypeService service;

  @GetMapping
  public ResponseEntity<PaginationDTO<LinkTypeDTO.Response>> getAllLinkExchanges(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @Min (0) @Max (100) @RequestParam(name = "per_page", defaultValue = "10") Integer perPage
  ) {
    PaginationDTO<LinkTypeDTO.Response> linkTypes =
        service.getAllLinkTypes(page, perPage);
    return ResponseEntity.ok(linkTypes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LinkTypeDTO.Response> getLinkTypeById(
      @PathVariable(name = "id") String id
  ) {
    LinkTypeDTO.Response link =
        service.getLinkTypeById(id);
    return ResponseEntity.ok(link);
  }

  @PostMapping
  public ResponseEntity<LinkTypeDTO.Response> createLinkType(
      @Valid @RequestBody LinkTypeDTO.Request request
  ) {
    LinkTypeDTO.Response createdLink = service.createLinkType(request);
    return ResponseEntity.ok(createdLink);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LinkTypeDTO.Response> updateLinkType(
      @PathVariable(name = "id") String id,
      @Valid @RequestBody LinkTypeDTO.Request request
  ) {
    LinkTypeDTO.Response updatedLink =
        service.updateLinkType(id, request);
    return ResponseEntity.ok(updatedLink);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLinkType(
      @PathVariable(name = "id") String id
  ) {
    service.deleteLinkType(id);
    return ResponseEntity.noContent().build();
  }
}
