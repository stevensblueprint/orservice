package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.RequiredDocumentDTO;
import java.util.List;

public interface RequiredDocumentService {
  RequiredDocumentDTO.Response createRequiredDocument(RequiredDocumentDTO.Request dto);
  List<RequiredDocumentDTO.Response> getRequiredDocumentByServiceId(String serviceId);
}
