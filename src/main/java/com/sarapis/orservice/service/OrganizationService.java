package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;

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
      Integer perPage,
      String format
  );

  OrganizationDTO.Response getOrganizationById(String id);
  OrganizationDTO.Response createOrganization(OrganizationDTO.Request requestDto);
  long writeCsv(ZipOutputStream zipOutputStream) throws IOException;
  long writePdf(ZipOutputStream zipOutputStream) throws IOException;
}
