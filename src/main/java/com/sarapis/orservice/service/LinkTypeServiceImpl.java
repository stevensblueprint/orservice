package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LinkTypeDTO;
import com.sarapis.orservice.dto.LinkTypeDTO.Request;
import com.sarapis.orservice.dto.LinkTypeDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.LinkTypeMapper;
import com.sarapis.orservice.model.LinkType;
import com.sarapis.orservice.repository.LinkTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkTypeServiceImpl implements LinkTypeService {
  private final LinkTypeRepository repository;
  private final LinkTypeMapper mapper;
  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllLinkTypes(Integer page, Integer perPage) {
    PageRequest pageable = PageRequest.of(page, perPage);
    Page<LinkType> linkTypesPage = repository.findAll(pageable);
    Page<LinkTypeDTO.Response> dtoPage = linkTypesPage.map(mapper::toResponseDTO);
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getLinkTypeById(String id) {
    LinkType linkType = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Link Type not found with ID" + id));
    return mapper.toResponseDTO(linkType);
  }

  @Override
  @Transactional
  public Response updateLinkType(String id, Request request) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Link Type not found with ID " + id);
    }
    request.setId(id);
    LinkType updatedLinkType = repository.save(mapper.toEntity(request));
    return mapper.toResponseDTO(updatedLinkType);
  }

  @Override
  public void deleteLinkType(String id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Link Type not found with ID " + id);
    }
    repository.deleteById(id);
  }
}
