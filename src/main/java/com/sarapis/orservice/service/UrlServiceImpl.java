package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.dto.UrlDTO.Request;
import com.sarapis.orservice.dto.UrlDTO.Response;
import com.sarapis.orservice.mapper.UrlMapper;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.UrlRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

  private final UrlRepository urlRepository;
  private final UrlMapper urlMapper;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createUrl(Request urlDto) {
    if (urlDto.getId() == null || urlDto.getId().trim().isEmpty()) {
      urlDto.setId(UUID.randomUUID().toString());
    }
    Url url = urlMapper.toEntity(urlDto);
    Url savedUrl = urlRepository.save(url);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedUrl.getId(),
        URL_RESOURCE_TYPE,
        CREATE.name(),
    "url",
        EMPTY_PREVIOUS_VALUE,
        urlMapper.toResponseDTO(savedUrl).toString(),
        "SYSTEM"
    );
    UrlDTO.Response response = urlMapper.toResponseDTO(savedUrl);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        url.getId(), URL_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getUrlsByOrganizationId(String organizationId) {
    List<Url> urls = urlRepository.findByOrganizationId(organizationId);
    List<UrlDTO.Response> urlDtos = urls.stream().map(urlMapper::toResponseDTO).toList();
    urlDtos = urlDtos.stream().peek(url -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          url.getId(), URL_RESOURCE_TYPE
      );
      url.setMetadata(metadata);
    }).toList();
    return urlDtos;
  }
}
