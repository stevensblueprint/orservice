package com.sarapis.orservice.service;

import com.amazonaws.util.IOUtils;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sarapis.orservice.dto.*;
import com.sarapis.orservice.dto.OrganizationDTO.Request;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.mapper.OrganizationMapper;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.OrganizationSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final UrlService urlService;
  private final FundingService fundingService;
  private final ContactService contactService;
  private final PhoneService phoneService;
  private final ProgramService programService;
  private final OrganizationIdentifierService organizationIdentifierService;
  private final LocationService locationService;
  private final MetadataService metadataService;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllOrganizations(String search, Boolean full_service,
      Boolean full, String taxonomyTerm, String taxonomyId, Integer page, Integer perPage,
      String format) {
    Specification<Organization> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(OrganizationSpecifications.hasSearchTerm(search));
    }

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);

    Page<OrganizationDTO.Response> dtoPage = organizationPage.map(organization -> {
      OrganizationDTO.Response response = organizationMapper.toResponseDTO(organization);
      response.setAdditionalWebsites(urlService.getUrlsByOrganizationId(organization.getId()));
      response.setFunding(fundingService.getFundingByOrganizationId(organization.getId()));
      response.setContacts(contactService.getContactsByOrganizationId(organization.getId()));
      response.setPhones(phoneService.getPhonesByOrganizationId(organization.getId()));
      response.setPrograms(programService.getProgramsByOrganizationId(organization.getId()));
      response.setOrganizationIdentifiers(
          organizationIdentifierService.getOrganizationIdentifiersByOrganizationId(organization.getId()));
      response.setLocations(locationService.getLocationByOrganizationId(organization.getId()));
      response.setMetadata(
          metadataService.getMetadataByResourceIdAndResourceType(organization.getId(), ORGANIZATION_RESOURCE_TYPE));
      return response;
    });

    return PaginationDTO.fromPage(dtoPage);
  }


  @Override
  @Transactional(readOnly = true)
  public Response getOrganizationById(String id) {
    Organization organization = organizationRepository.findById(id).orElseThrow();
    Response response = organizationMapper.toResponseDTO(organization);
    response.setAdditionalWebsites(urlService.getUrlsByOrganizationId(organization.getId()));
    response.setFunding(fundingService.getFundingByOrganizationId(organization.getId()));
    response.setContacts(contactService.getContactsByOrganizationId(organization.getId()));
    response.setPhones(phoneService.getPhonesByOrganizationId(organization.getId()));
    response.setPrograms(programService.getProgramsByOrganizationId(organization.getId()));
    response.setOrganizationIdentifiers(
        organizationIdentifierService.getOrganizationIdentifiersByOrganizationId(organization.getId()));
    response.setLocations(locationService.getLocationByOrganizationId(organization.getId()));
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(organization.getId(), ORGANIZATION_RESOURCE_TYPE));
    return response;
  }

  @Override
  @Transactional
  public Response createOrganization(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Organization organization = organizationMapper.toEntity(dto);
    Organization savedOrganization = organizationRepository.save(organization);

    MetadataUtils.createMetadataEntry(
        metadataService,
        savedOrganization.getId(),
        ORGANIZATION_RESOURCE_TYPE,
        CREATE.name(),
        "organization",
        EMPTY_PREVIOUS_VALUE,
        organizationMapper.toResponseDTO(savedOrganization).toString(),
        "SYSTEM"
    );

    List<UrlDTO.Response> savedUrls = new ArrayList<>();
    if (dto.getAdditionalWebsites() != null) {
      for (UrlDTO.Request urlDTO : dto.getAdditionalWebsites()) {
        urlDTO.setOrganizationId(savedOrganization.getId());
        UrlDTO.Response savedUrl = urlService.createUrl(urlDTO);
        savedUrls.add(savedUrl);
      }
    }

    List<FundingDTO.Response> savedFunding = new ArrayList<>();
    if (dto.getFunding() != null) {
      for (FundingDTO.Request fundingDTO : dto.getFunding()) {
        fundingDTO.setOrganizationId(savedOrganization.getId());
        FundingDTO.Response savedFundingItem = fundingService.createFunding(fundingDTO);
        savedFunding.add(savedFundingItem);
      }
    }

    List<ContactDTO.Response> savedContacts = new ArrayList<>();
    if (dto.getContacts() != null) {
      for (ContactDTO.Request contactDTO : dto.getContacts()) {
        contactDTO.setOrganizationId(savedOrganization.getId());
        ContactDTO.Response savedContact = contactService.createContact(contactDTO);
        savedContacts.add(savedContact);
      }
    }

    List<PhoneDTO.Response> savedPhones = new ArrayList<>();
    if (dto.getPhones() != null) {
      for (PhoneDTO.Request phoneDTO : dto.getPhones()) {
        phoneDTO.setOrganizationId(savedOrganization.getId());
        PhoneDTO.Response savedPhone = phoneService.createPhone(phoneDTO);
        savedPhones.add(savedPhone);
      }
    }

    List<ProgramDTO.Response> savedPrograms = new ArrayList<>();
    if (dto.getPrograms() != null) {
      for (ProgramDTO.Request programDTO : dto.getPrograms()) {
        programDTO.setOrganizationId(savedOrganization.getId());
        ProgramDTO.Response savedProgram = programService.createProgram(programDTO);
        savedPrograms.add(savedProgram);
      }
    }

    List<OrganizationIdentifierDTO.Response> savedOrganizationIdentifiers = new ArrayList<>();
    if (dto.getOrganizationIdentifiers() != null) {
      for (OrganizationIdentifierDTO.Request organizationIdentifierDTO : dto.getOrganizationIdentifiers()) {
        organizationIdentifierDTO.setOrganizationId(savedOrganization.getId());
        OrganizationIdentifierDTO.Response savedOrganizationIdentifier =
            organizationIdentifierService.createOrganizationIdentifier(organizationIdentifierDTO);
        savedOrganizationIdentifiers.add(savedOrganizationIdentifier);
      }
    }

    List<LocationDTO.Response> savedLocations = new ArrayList<>();
    if (dto.getLocations() != null) {
      for (LocationDTO.Request locationDTO : dto.getLocations()) {
        locationDTO.setOrganizationId(savedOrganization.getId());
        LocationDTO.Response savedLocation = locationService.createLocation(locationDTO);
        savedLocations.add(savedLocation);
      }
    }

    List<MetadataDTO.Response> metadata =
        metadataService.getMetadataByResourceIdAndResourceType(organization.getId(), ORGANIZATION_RESOURCE_TYPE);
    Response response = organizationMapper.toResponseDTO(savedOrganization);
    response.setAdditionalWebsites(savedUrls);
    response.setFunding(savedFunding);
    response.setContacts(savedContacts);
    response.setPhones(savedPhones);
    response.setPrograms(savedPrograms);
    response.setOrganizationIdentifiers(savedOrganizationIdentifiers);
    response.setLocations(savedLocations);
    response.setMetadata(metadata);
    return response;
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
    PdfWriter.getInstance(document, zipOutputStream);
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
}
