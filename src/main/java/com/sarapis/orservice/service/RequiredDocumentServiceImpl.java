package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.REQUIRED_DOCUMENT_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.RequiredDocumentDTO;
import com.sarapis.orservice.dto.RequiredDocumentDTO.Request;
import com.sarapis.orservice.dto.RequiredDocumentDTO.Response;
import com.sarapis.orservice.mapper.RequiredDocumentMapper;
import com.sarapis.orservice.model.RequiredDocument;
import com.sarapis.orservice.repository.RequiredDocumentRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequiredDocumentServiceImpl implements RequiredDocumentService {
  private final RequiredDocumentRepository repository;
  private final RequiredDocumentMapper mapper;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createRequiredDocument(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    RequiredDocument document = mapper.toEntity(dto);
    RequiredDocument savedDocument = repository.save(document);
    metadataService.createMetadata(
        null,
        savedDocument,
        REQUIRED_DOCUMENT_RESOURCE_TYPE,
        CREATE,
        MetadataUtils.DEFAULT_CREATED_BY
    );
    RequiredDocumentDTO.Response response = mapper.toResponseDTO(savedDocument);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedDocument.getId(), REQUIRED_DOCUMENT_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getRequiredDocumentByServiceId(String serviceId) {
    List<RequiredDocument> documents = repository.findRequiredDocumentByServiceId(serviceId);
    List<RequiredDocumentDTO.Response> documentDtos = documents.stream().map(mapper::toResponseDTO).toList();
    documentDtos = documentDtos.stream().peek(document -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          document.getId(), REQUIRED_DOCUMENT_RESOURCE_TYPE
      );
      document.setMetadata(metadata);
    }).toList();
    return documentDtos;
  }
}
