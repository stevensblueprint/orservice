package com.sarapis.orservice.service;

import com.amazonaws.util.IOUtils;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.mapper.LocationMapper;
import com.sarapis.orservice.model.Location;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
  private final LocationRepository locationRepository;
  private final LocationMapper locationMapper;
  private final MetadataRepository metadataRepository;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public LocationDTO.Response createLocation(LocationDTO.Request requestDto, String updatedBy) {
    if (requestDto.getId() == null || StringUtils.isBlank(requestDto.getId())) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Location location = locationMapper.toEntity(requestDto);
    location.setMetadata(metadataRepository, updatedBy);
    Location savedLocation = locationRepository.save(location);
    return locationMapper.toResponseDTO(savedLocation, metadataService);
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
    for (Location location : locationRepository.findAll()) {
      csvPrinter.printRecord(LocationDTO.toExport(location));
    }
    // Flushes to zip entry
    csvPrinter.flush();
    ZipEntry entry = new ZipEntry("locations.csv");
    zipOutputStream.putNextEntry(entry);
    IOUtils.copy(new ByteArrayInputStream(out.toByteArray()), zipOutputStream);
    zipOutputStream.closeEntry();
    return entry.getSize();
  }

  @Override
  public long writePdf(ZipOutputStream zipOutputStream) throws IOException {
    // Sets PDF document to write directly to zip entry stream
    ZipEntry entry = new ZipEntry("locations.pdf");
    zipOutputStream.putNextEntry(entry);
    com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
    PdfWriter writer = PdfWriter.getInstance(document, zipOutputStream);
    writer.setCloseStream(false);
    document.open();
    // Sets table
    PdfPTable table = new PdfPTable(10);
    PdfPCell cell = new PdfPCell();
    // Sets table header
    LocationDTO.EXPORT_HEADER.forEach(column -> {
      cell.setPhrase(new Phrase(column));
      table.addCell(cell);
    });
    // Sets table entries
    locationRepository.findAll()
      .forEach(location -> LocationDTO.toExport(location)
        .forEach(table::addCell));
    document.add(table);
    document.close();
    zipOutputStream.closeEntry();
    return entry.getSize();
  }

  @Override
  @Transactional
  public void readCsv(MultipartFile file, String updatedBy, List<String> metadataIds) throws IOException {
    LocationDTO.csvToLocations(file.getInputStream()).forEach(createRequest -> {
      LocationDTO.Response response = createLocation(createRequest, updatedBy);
      metadataIds.addAll(response.getMetadata().stream().map(MetadataDTO.Response::getId).toList());
    });
  }
}
