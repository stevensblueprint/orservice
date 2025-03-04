package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.UrlDTO.Request;
import com.sarapis.orservice.dto.UrlDTO.Response;
import com.sarapis.orservice.mapper.UrlMapper;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.UrlRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

  private final UrlRepository urlRepository;
  private final UrlMapper urlMapper;

  @Override
  @Transactional
  public Response createUrl(Request urlDto) {
    if (urlDto.getId() == null || urlDto.getId().trim().isEmpty()) {
      urlDto.setId(UUID.randomUUID().toString());
    }
    Url url = urlMapper.toEntity(urlDto);
    Url savedUrl = urlRepository.save(url);
    return urlMapper.toResponseDTO(savedUrl);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getUrlsByOrganizationId(String organizationId) {
    List<Url> urls = urlRepository.findByOrganizationId(organizationId);
    return urls.stream().map(urlMapper::toResponseDTO).collect(Collectors.toList());
  }
}
