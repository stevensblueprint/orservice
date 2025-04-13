package com.sarapis.orservice.service;

import com.amazonaws.util.IOUtils;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationDTO.Request;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.OrganizationMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.OrganizationSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import static com.sarapis.orservice.utils.FieldMap.ORGANIZATION_FIELD_MAP;

import java.util.List;
import java.util.UUID;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final MetadataService metadataService;
  private final MetadataRepository metadataRepository;

  private static final boolean RETURN_FULL_SERVICE = true;
  private static final int RECORDS_PER_STREAM = 100;


  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllOrganizations(String search, Boolean full_service,
      Boolean full, String taxonomyTerm, String taxonomyId, Integer page, Integer perPage) {
    Specification<Organization> spec = buildSpecification(search, taxonomyTerm, taxonomyId);

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);
    Page<OrganizationDTO.Response> dtoPage = organizationPage
        .map(organization -> organizationMapper.toResponseDTO(organization, metadataService, full_service));

    return PaginationDTO.fromPage(dtoPage);
  }

  // In OrganizationServiceImpl.java
  @Override
  @Transactional(readOnly = true)
  public void streamAllOrganizations(String search, Boolean fullService, Boolean full,
      String taxonomyTerm, String taxonomyId, Consumer<OrganizationDTO.Response> consumer) {

    Specification<Organization> spec = buildSpecification(search, taxonomyTerm, taxonomyId);
    int currentPage = 0;
    boolean hasMoreData = true;

    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);

      List<Organization> organizations = organizationPage.getContent();

      if (organizations.isEmpty()) {
        hasMoreData = false;
      } else {
        organizations.forEach(organization ->
            consumer.accept(organizationMapper.toResponseDTO(organization, metadataService, fullService))
        );
        if (currentPage >= organizationPage.getTotalPages() - 1) {
          hasMoreData = false;
        } else {
          currentPage++;
        }
      }
    }
  }


  @Override
  @Transactional(readOnly = true)
  public Response getOrganizationById(String id, Boolean fullService) {
    Organization organization = organizationRepository.findById(id).orElseThrow();
    return organizationMapper.toResponseDTO(organization, metadataService, fullService);
  }

  @Override
  @Transactional
  public Response createOrganization(Request requestDto, String updatedBy) {
    if (requestDto.getId() == null || requestDto.getId().trim().isEmpty()) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Organization organization = organizationMapper.toEntity(requestDto);
    organization.setMetadata(metadataRepository, updatedBy);
    Organization savedOrganization = organizationRepository.save(organization);
    return organizationMapper.toResponseDTO(savedOrganization, metadataService, RETURN_FULL_SERVICE);
  }

  @Override
  @Transactional
  public void deleteOrganization(String id) {
    organizationRepository.deleteById(id);
    log.info("Deleted organization with id: {}", id);
  }

  @Override
  @Transactional
  public Response undoOrganizationMetadata(String metadataId, String updatedBy) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new ResourceNotFoundException("Metadata", metadataId));

    Organization reverted = MetadataUtils.undoMetadata(
        metadata,
        this.metadataRepository,
        this.organizationRepository,
        ORGANIZATION_FIELD_MAP,
        updatedBy
    );
    return organizationMapper.toResponseDTO(reverted, metadataService, RETURN_FULL_SERVICE);
  }
  private Specification<Organization> buildSpecification(String search, String taxonomyTerm, String taxonomyId) {
    Specification<Organization> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(OrganizationSpecifications.hasSearchTerm(search));
    }
    return spec;
  }

  @Override
  public long writeCsv(ZipOutputStream zipOutputStream) throws IOException {
    // Sets CSV printer
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);
    // Sets CSV header
    csvPrinter.printRecord(OrganizationDTO.EXPORT_HEADER);
    // Sets CSV entries
    for (Organization organization : organizationRepository.findAll()) {
      csvPrinter.printRecord(OrganizationDTO.toExport(organization));
    }
    // Flushes to zip entry
    csvPrinter.flush();
    ZipEntry entry = new ZipEntry("organizations.csv");
    zipOutputStream.putNextEntry(entry);
    IOUtils.copy(new ByteArrayInputStream(out.toByteArray()), zipOutputStream);
    zipOutputStream.closeEntry();
    return entry.getSize();
  }

  @Override
  public long writePdf(ZipOutputStream zipOutputStream) throws IOException {
    // Sets PDF document to write directly to zip entry stream
    ZipEntry entry = new ZipEntry("organizations.pdf");
    zipOutputStream.putNextEntry(entry);
    com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
    PdfWriter writer = PdfWriter.getInstance(document, zipOutputStream);
    writer.setCloseStream(false);
    document.open();
    // Sets table
    PdfPTable table = new PdfPTable(10);
    PdfPCell cell = new PdfPCell();
    // Sets table header
    OrganizationDTO.EXPORT_HEADER.forEach(column -> {
      cell.setPhrase(new Phrase(column));
      table.addCell(cell);
    });
    // Sets table entries
    for (Organization organization : organizationRepository.findAll()) {
      OrganizationDTO.toExport(organization).forEach(table::addCell);
    }
    document.add(table);
    document.close();
    zipOutputStream.closeEntry();
    return entry.getSize();
  }

  @Override
  @Transactional
  public void readCsv(MultipartFile file, String updatedBy, List<String> metadataIds) throws IOException {
    OrganizationDTO.csvToOrganizations(file.getInputStream()).forEach(createRequest -> {
      OrganizationDTO.Response response = createOrganization(createRequest, updatedBy);
      metadataIds.addAll(response.getMetadata().stream().map(MetadataDTO.Response::getId).toList());
    });
  }
}
