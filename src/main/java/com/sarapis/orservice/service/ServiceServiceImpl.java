package com.sarapis.orservice.service;

import com.amazonaws.util.IOUtils;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Request;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.ServiceMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.ServiceSpecifications;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.sarapis.orservice.utils.FieldMap.SERVICE_FIELD_MAP;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
  private final ServiceRepository serviceRepository;
  private final ServiceMapper serviceMapper;
  private final MetadataRepository metadataRepository;
  private final MetadataService metadataService;

  private static final int RECORDS_PER_STREAM = 100;
  private static final String FILENAME = DataExchangeDTO.ExchangeableFile.SERVICE.toFileName();

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServices(String search, Integer page, Integer perPage,
      String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean minimal, Boolean full) {
    Specification<Service> spec = buildSpecification(search, modifiedAfter, taxonomyTermId, taxonomyId, organizationId);
    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Service> servicePage = serviceRepository.findAll(spec, pageable);
    Page<ServiceDTO.Response> dtoPage = servicePage.map(service -> serviceMapper.toResponseDTO(service, metadataService));
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public void streamAllServices(String search, String taxonomyTermId, String taxonomyId,
      String organizationId, String modifiedAfter, Boolean minimal, Boolean full,
      Consumer<Response> consumer) {
    Specification<Service> spec = buildSpecification(search, modifiedAfter, taxonomyTermId, taxonomyId, organizationId);
    int currentPage = 0;
    boolean hasMoreData = true;
    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<Service> servicePage = serviceRepository.findAll(spec, pageable);
      List<Service> services = servicePage.getContent();

      if (services.isEmpty()) {
        hasMoreData = false;
      } else {
        services.forEach(service ->
            consumer.accept(serviceMapper.toResponseDTO(service, metadataService))
        );
        if (currentPage >= servicePage.getTotalPages() - 1) {
          hasMoreData = false;
        } else {
          currentPage++;
        }
      }
    }
  }


  @Override
  @Transactional(readOnly = true)
  public Response getServiceById(String id) {
    Service service = serviceRepository.findById(id).orElseThrow();
    return serviceMapper.toResponseDTO(service, metadataService);
  }

  @Override
  @Transactional
  public Response createService(Request requestDto, String updatedBy) {
    if (requestDto.getId() == null || StringUtils.isBlank(requestDto.getId())) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Service service = serviceMapper.toEntity(requestDto);
    service.setMetadata(metadataRepository, updatedBy);
    Service savedService = serviceRepository.save(service);
    return serviceMapper.toResponseDTO(savedService, metadataService);
  }

  @Override
  @Transactional
  public Response updateService(String id, Request updatedDto, String updatedBy) {
    if(!this.serviceRepository.existsById(id)) {
      throw new ResourceNotFoundException("Service", id);
    }

    updatedDto.setId(id);
    Service newService = this.serviceMapper.toEntity(updatedDto);

    Service updatedService = this.serviceRepository.save(newService);
    return this.serviceMapper.toResponseDTO(updatedService, this.metadataService);
  }

  @Override
  @Transactional
  public Response undoServiceMetadata(String metadataId, String updatedBy) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new ResourceNotFoundException("Metadata", metadataId));

    Service reverted = MetadataUtils.undoMetadata(
            metadata,
            this.metadataRepository,
            this.serviceRepository,
            SERVICE_FIELD_MAP,
            updatedBy
    );
    return serviceMapper.toResponseDTO(reverted, metadataService);
  }

  private Specification<Service> buildSpecification(String search, String modifiedAfter,
      String taxonomyTermId, String taxonomyId, String organizationId) {
    Specification<Service> spec = Specification.where(null);

    if (search!= null &&!search.isEmpty()) {
      spec = spec.and(ServiceSpecifications.hasSearchTerm(search));
    }

    if (modifiedAfter!= null &&!modifiedAfter.isEmpty()) {
      LocalDate modifiedDate = LocalDate.parse(modifiedAfter);
      spec = spec.and(ServiceSpecifications.modifiedAfter(modifiedDate));
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
    csvPrinter.printRecord(ServiceDTO.EXPORT_HEADER);
    // Sets CSV entries
    for (Service service : serviceRepository.findAll()) {
      csvPrinter.printRecord(ServiceDTO.toExport(service));
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
    ServiceDTO.EXPORT_HEADER.forEach(column -> {
      cell.setPhrase(new Phrase(column));
      table.addCell(cell);
    });
    // Sets table entries
    serviceRepository.findAll()
        .forEach(service -> ServiceDTO.toExport(service)
          .forEach(table::addCell));
    document.add(table);
    document.close();
    zipOutputStream.closeEntry();
    return entry.getSize();
  }

  @Override
  @Transactional
  public void readCsv(MultipartFile file, String updatedBy, List<String> metadataIds) throws IOException {
    ServiceDTO.csvToServices(file.getInputStream()).forEach(createRequest -> {
      ServiceDTO.Response response = createService(createRequest, updatedBy);
      metadataIds.addAll(response.getMetadata().stream().map(MetadataDTO.Response::getId).toList());
    });
  }
}
