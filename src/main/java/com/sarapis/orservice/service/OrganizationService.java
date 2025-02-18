package com.sarapis.orservice.service;

import com.lowagie.text.pdf.PdfPTable;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationDTO;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

public interface OrganizationService {
    List<OrganizationDTO> getAllOrganizations();

    OrganizationDTO getOrganizationById(String organizationId);

    OrganizationDTO createOrganization(UpsertOrganizationDTO upsertOrganizationDTO);

    OrganizationDTO updateOrganization(String organizationId, OrganizationDTO organizationDTO);

    void deleteOrganization(String organizationId);

    void writeCsv(ZipOutputStream zipOutputStream);

    void writePdf(ZipOutputStream zipOutputStream);
}
