package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO.Response;
import com.sarapis.orservice.mapper.AttributeMapper;
import com.sarapis.orservice.model.Attribute;
import com.sarapis.orservice.repository.AttributeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {
  private final AttributeRepository repository;
  private final AttributeMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public List<Response> getAttributeByLinkIdAndLinkType(String linkId, String linkType) {
    List<Attribute> attributes = repository.findByLinkIdAndLinkType(linkId, linkType);
    return attributes.stream().map(mapper::toResponseDTO).toList();
  }
}
