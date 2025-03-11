package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.Metadata.UPDATE;
import static com.sarapis.orservice.utils.MetadataUtils.DEFAULT_CREATED_BY;
import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.dto.UrlDTO.Request;
import com.sarapis.orservice.dto.UrlDTO.Response;
import com.sarapis.orservice.mapper.UrlMapper;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.UrlRepository;
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
    metadataService.createMetadata(
        null,
        savedUrl,
        URL_RESOURCE_TYPE,
        CREATE,
        DEFAULT_CREATED_BY
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
    return null;
  }

  @Override
  @Transactional
  public Response updateUrl(String id, Request urlDto) {
    Url url = urlRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("URL with ID " + id + " not found"));

    UrlDTO.Response previousState = urlMapper.toResponseDTO(url);
    Url updatedUrl = urlRepository.save(url);
    UrlDTO.Response updatedState = urlMapper.toResponseDTO(updatedUrl);

    metadataService.createMetadata(
        previousState,
        updatedUrl,
        URL_RESOURCE_TYPE,
        UPDATE,
        DEFAULT_CREATED_BY
    );

    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        updatedUrl.getId(), URL_RESOURCE_TYPE
    );
    updatedState.setMetadata(metadata);
    return updatedState;
  }
}
