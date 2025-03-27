package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import java.util.function.Consumer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public interface OrganizationService {
  PaginationDTO<OrganizationDTO.Response> getAllOrganizations(
      String search,
      Boolean full_service,
      Boolean full,
      String taxonomyTerm,
      String taxonomyId,
      Integer page,
      Integer perPage
  );

  OrganizationDTO.Response getOrganizationById(String id, Boolean fullService);
  void streamAllOrganizations(String search, Boolean fullService, Boolean full,
      String taxonomyTerm, String taxonomyId, Consumer<OrganizationDTO.Response> consumer);
  OrganizationDTO.Response createOrganization(OrganizationDTO.Request requestDto, String updatedBy);
  void deleteOrganization(String id);

  long writeCsv(ZipOutputStream zipOutputStream) throws IOException;
  long writePdf(ZipOutputStream zipOutputStream) throws IOException;
}
