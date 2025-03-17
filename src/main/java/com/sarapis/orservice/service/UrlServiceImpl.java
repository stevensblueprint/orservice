package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.dto.UrlDTO.Request;
import com.sarapis.orservice.dto.UrlDTO.Response;
import com.sarapis.orservice.mapper.UrlMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.UrlRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;

import com.sarapis.orservice.utils.Parser;
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

  @Override
  @Transactional
  public void undoUrlMetadata(Metadata metadata) {
    String resType = metadata.getResourceType();
    if(!resType.equals(URL_RESOURCE_TYPE)) {
      throw new RuntimeException("Mismatched metadata to revert");
    }

    if(MetadataUtils.targetsEntity(metadata)) {
        this.undoEntityMetadata(metadata);
    }
    else {
        String fieldName = metadata.getFieldName();
        this.undoFieldMetadata(metadata, fieldName);
    }

    String oldAction = metadata.getLastActionType();
    String newAction = MetadataUtils.getComplementAction(oldAction);
    MetadataUtils.createMetadataEntry(
            this.metadataService,
            metadata.getResourceId(),
            metadata.getResourceType(),
            newAction,
            metadata.getFieldName(),
            metadata.getReplacementValue(),
            metadata.getPreviousValue(),
            "SYSTEM"    // TODO: User Invoker?
    );
  }

  private void undoEntityMetadata(Metadata metadata) {
      String resId = metadata.getResourceId();
      String actionType = metadata.getLastActionType();

      if(actionType.equals(CREATE.toString())) {
          Url url = this.urlRepository.findById(resId)
                  .orElseThrow();
          this.urlRepository.delete(url);
      }
      else {
          String rawUrlEntity = metadata.getPreviousValue();
          Url url = Parser.parseToEntity(rawUrlEntity, Url.class);
          this.urlRepository.save(url);
      }
  }

  private void undoFieldMetadata(Metadata metadata, String fieldName) {
      String resId = metadata.getResourceId();
      Url url = this.urlRepository.findById(resId)
              .orElseThrow();

      String prevValue = metadata.getPreviousValue();
      switch(fieldName) {
          case "label":
              url.setLabel(prevValue);
              break;
          case "url":
              url.setUrl(prevValue);
              break;
          case "organizationId":
              url.setOrganizationId(prevValue);
              break;
          case "serviceId":
              url.setServiceId(prevValue);
              break;
      }

      this.urlRepository.save(url);
  }
}
