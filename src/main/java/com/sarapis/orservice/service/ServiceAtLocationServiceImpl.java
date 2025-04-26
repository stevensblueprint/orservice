package com.sarapis.orservice.service;


import com.amazonaws.util.IOUtils;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sarapis.orservice.dto.*;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Request;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Response;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.ServiceAtLocationMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.ServiceAtLocation;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import com.sarapis.orservice.repository.ServiceAtLocationSpecifications;
import com.sarapis.orservice.utils.DataExchangeUtils;
import com.sarapis.orservice.utils.MetadataUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
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

import static com.sarapis.orservice.utils.FieldMap.SERVICE_AT_LOCATION_FIELD_MAP;

@Service
@RequiredArgsConstructor
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {
  private final ServiceAtLocationRepository serviceAtLocationRepository;
  private final ServiceAtLocationMapper serviceAtLocationMapper;
  private final MetadataService metadataService;
  private final MetadataRepository metadataRepository;

  private static final int RECORDS_PER_STREAM = 100;
  private static final String FILENAME = DataExchangeDTO.ExchangeableFile.SERVICE_AT_LOCATION.toFileName();

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServicesAtLocation(String search, String taxonomyTermId,
                                                          String taxonomyId, String organizationId, String modifiedAfter, Boolean full, Integer page,
                                                          Integer perPage, String postcode, String proximity) {
    Specification<ServiceAtLocation> spec = buildSpecification(search, taxonomyTermId, taxonomyId,
      organizationId, modifiedAfter, postcode, proximity);

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<ServiceAtLocation> serviceAtLocationPage = serviceAtLocationRepository.findAll(spec, pageable);
    Page<ServiceAtLocationDTO.Response> dtoPage =
      serviceAtLocationPage.map(serviceAtLocation -> serviceAtLocationMapper.toResponseDTO(serviceAtLocation, metadataService));
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public void streamAllServicesAtLocation(String search, String taxonomyTermId, String taxonomyId, String organizationId,
                                          String modifiedAfter, Boolean full, String postcode, String proximity, Consumer<Response> consumer) {
    Specification<ServiceAtLocation> spec = buildSpecification(search, taxonomyTermId, taxonomyId,
      organizationId, modifiedAfter, postcode, proximity);
    int currentPage = 0;
    boolean hasMoreData = true;
    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<ServiceAtLocation> services = serviceAtLocationRepository.findAll(spec, pageable);
      List<ServiceAtLocation> serviceList = services.getContent();
      if (serviceList.isEmpty()) {
        hasMoreData = false;
      } else {
        services.forEach(service -> {
          consumer.accept(serviceAtLocationMapper.toResponseDTO(service, metadataService));
        });
        if (currentPage >= services.getTotalPages() - 1) {
          hasMoreData = false;
        } else {
          currentPage++;
        }
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Response getServiceAtLocationById(String id) {
    ServiceAtLocation service = serviceAtLocationRepository.findById(id).orElseThrow();
    return serviceAtLocationMapper.toResponseDTO(service, metadataService);
  }

  @Override
  @Transactional
  public Response updateServiceAtLocation(String id, Request updatedDto, String updatedBy) {
    if (!this.serviceAtLocationRepository.existsById(id)) {
      throw new ResourceNotFoundException("ServiceAtLocation", id);
    }

    updatedDto.setId(id);
    ServiceAtLocation newServLoc = this.serviceAtLocationMapper.toEntity(updatedDto);

    ServiceAtLocation updatedServLoc = this.serviceAtLocationRepository.save(newServLoc);
    return this.serviceAtLocationMapper.toResponseDTO(updatedServLoc, this.metadataService);
  }

  @Override
  @Transactional
  public Response undoServiceAtLocationMetadata(String metadataId, String updatedBy) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
      .orElseThrow(() -> new ResourceNotFoundException("Metadata", metadataId));

    ServiceAtLocation reverted = MetadataUtils.undoMetadata(
      metadata,
      this.metadataRepository,
      this.serviceAtLocationRepository,
      SERVICE_AT_LOCATION_FIELD_MAP,
      updatedBy
    );
    return serviceAtLocationMapper.toResponseDTO(reverted, metadataService);
  }

  @Override
  @Transactional
  public Response createServiceAtLocation(Request dto, String updatedBy) {
    if (dto.getId() == null || StringUtils.isBlank(dto.getId())) {
      dto.setId(UUID.randomUUID().toString());
    }
    ServiceAtLocation serviceAtLocation = serviceAtLocationMapper.toEntity(dto);
    serviceAtLocation.setMetadata(metadataRepository, updatedBy);
    ServiceAtLocation savedServiceAtLocation = serviceAtLocationRepository.save(serviceAtLocation);
    return serviceAtLocationMapper.toResponseDTO(savedServiceAtLocation, metadataService);
  }

  private Specification<ServiceAtLocation> buildSpecification(String search, String taxonomyTermId,
                                                              String taxonomyId, String organizationId, String modifiedAfter, String postcode, String proximity) {
    Specification<ServiceAtLocation> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(ServiceAtLocationSpecifications.hasSearchTerm(search));
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
    csvPrinter.printRecord(LocationDTO.EXPORT_HEADER);
    // Sets CSV entries
    for (ServiceAtLocation serviceAtLocation : serviceAtLocationRepository.findAll()) {
      csvPrinter.printRecord(ServiceAtLocationDTO.toExport(serviceAtLocation));
    }
    // Flushes to zip entry
    csvPrinter.flush();
    ZipEntry entry = new ZipEntry(DataExchangeUtils.addExtension(FILENAME, DataExchangeUtils.CSV_EXTENSION));
    zipOutputStream.putNextEntry(entry);
    IOUtils.copy(new ByteArrayInputStream(out.toByteArray()), zipOutputStream);
    zipOutputStream.closeEntry();
    return entry.getSize();
  }

  @Override
  public long writePdf(ZipOutputStream zipOutputStream) throws IOException {
    // Sets PDF document to write directly to zip entry stream
    ZipEntry entry = new ZipEntry(DataExchangeUtils.addExtension(FILENAME, DataExchangeUtils.PDF_EXTENSION));
    zipOutputStream.putNextEntry(entry);
    com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
    PdfWriter writer = PdfWriter.getInstance(document, zipOutputStream);
    writer.setCloseStream(false);
    document.open();
    // Sets table
    PdfPTable table = new PdfPTable(10);
    PdfPCell cell = new PdfPCell();
    // Sets table header
    ServiceAtLocationDTO.EXPORT_HEADER.forEach(column -> {
      cell.setPhrase(new Phrase(column));
      table.addCell(cell);
    });
    // Sets table entries
    serviceAtLocationRepository.findAll()
      .forEach(serviceAtLocation -> ServiceAtLocationDTO.toExport(serviceAtLocation)
        .forEach(table::addCell));
    document.add(table);
    document.close();
    zipOutputStream.closeEntry();
    return entry.getSize();
  }

  @Override
  @Transactional
  public void readCsv(MultipartFile file, String updatedBy, List<String> metadataIds) throws IOException {
    ServiceAtLocationDTO.csvToServiceAtLocations(file.getInputStream()).forEach(createRequest -> {
      ServiceAtLocationDTO.Response response = createServiceAtLocation(createRequest, updatedBy);
      metadataIds.addAll(response.getMetadata().stream().map(MetadataDTO.Response::getId).toList());
    });
  }
}
