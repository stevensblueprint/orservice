package com.sarapis.orservice.service;

import com.amazonaws.util.IOUtils;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationIdentifierDTO;
import com.sarapis.orservice.entity.*;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UrlRepository urlRepository;
    private final FundingRepository fundingRepository;
    private final AttributeService attributeService;
    private final LocationRepository locationRepository;
    private final PhoneRepository phoneRepository;
    private final ContactRepository contactRepository;
    private final ProgramRepository programRepository;
    private final OrganizationIdentifierRepository organizationIdentifierRepository;
    private final MetadataService metadataService;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   UrlRepository urlRepository,
                                   FundingRepository fundingRepository,
                                   AttributeService attributeService,
                                   LocationRepository locationRepository,
                                   PhoneRepository phoneRepository,
                                   ContactRepository contactRepository,
                                   ProgramRepository programRepository,
                                   OrganizationIdentifierRepository organizationIdentifierRepository,
                                   MetadataService metadataService) {
        this.organizationRepository = organizationRepository;
        this.urlRepository = urlRepository;
        this.fundingRepository = fundingRepository;
        this.attributeService = attributeService;
        this.locationRepository = locationRepository;
        this.phoneRepository = phoneRepository;
        this.contactRepository = contactRepository;
        this.programRepository = programRepository;
        this.organizationIdentifierRepository = organizationIdentifierRepository;
        this.metadataService = metadataService;
    }

    @Override
    public List<OrganizationDTO> getAllOrganizations() {
        List<OrganizationDTO> organizationDTOs = this.organizationRepository.findAll()
                .stream().map(Organization::toDTO).toList();
        organizationDTOs.forEach(this::addRelatedData);
        return organizationDTOs;
    }

    @Override
    public OrganizationDTO getOrganizationById(String organizationId) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        OrganizationDTO organizationDTO = organization.toDTO();
        this.addRelatedData(organizationDTO);
        return organizationDTO;
    }

    @Override
    public OrganizationDTO createOrganization(UpsertOrganizationDTO upsertOrganizationDTO) {
        Organization createdOrganization = this.organizationRepository.save(upsertOrganizationDTO.create());

        if (upsertOrganizationDTO.getParentOrganizationId() != null) {
            Organization parentOrganization = this.organizationRepository
                    .findById(upsertOrganizationDTO.getParentOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Parent organization not found."));
            createdOrganization.setParentOrganization(parentOrganization);
        }

        createdOrganization.setAdditionalWebsites(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getAdditionalWebsites()) {
            Url url = this.urlRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Url not found."));
            url.setOrganization(createdOrganization);
            this.urlRepository.save(url);
            createdOrganization.getAdditionalWebsites().add(url);
        }

        createdOrganization.setFunding(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getFundings()) {
            Funding funding = this.fundingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Funding not found."));
            funding.setOrganization(createdOrganization);
            this.fundingRepository.save(funding);
            createdOrganization.getFunding().add(funding);
        }

        createdOrganization.setLocations(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getLocations()) {
            Location location = this.locationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Location not found."));
            location.setOrganization(createdOrganization);
            this.locationRepository.save(location);
            createdOrganization.getLocations().add(location);
        }

        createdOrganization.setPhones(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getPhones()) {
            Phone phone = this.phoneRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Phone not found."));
            phone.setOrganization(createdOrganization);
            this.phoneRepository.save(phone);
            createdOrganization.getPhones().add(phone);
        }

        createdOrganization.setContacts(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getContacts()) {
            Contact contact = this.contactRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contact not found."));
            contact.setOrganization(createdOrganization);
            this.contactRepository.save(contact);
            createdOrganization.getContacts().add(contact);
        }

        createdOrganization.setPrograms(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getPrograms()) {
            Program program = this.programRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Program not found."));
            program.setOrganization(createdOrganization);
            this.programRepository.save(program);
            createdOrganization.getPrograms().add(program);
        }

        createdOrganization.setOrganizationIdentifiers(new ArrayList<>());
        for (UpsertOrganizationIdentifierDTO dto : upsertOrganizationDTO.getOrganizationIdentifiers()) {
            OrganizationIdentifier organizationIdentifier = dto.create();
            organizationIdentifier.setOrganization(createdOrganization);
            OrganizationIdentifier createdOrganizationIdentifier = this.organizationIdentifierRepository.save(organizationIdentifier);
            createdOrganization.getOrganizationIdentifiers().add(createdOrganizationIdentifier);
        }

        this.organizationRepository.save(createdOrganization);
        return this.getOrganizationById(createdOrganization.getId());
    }

    @Override
    public OrganizationDTO updateOrganization(String organizationId, OrganizationDTO organizationDTO) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found."));

        organization.setName(organizationDTO.getName());
        organization.setAlternateName(organizationDTO.getAlternateName());
        organization.setDescription(organizationDTO.getDescription());
        organization.setEmail(organizationDTO.getEmail());
        organization.setWebsite(organizationDTO.getWebsite());
        organization.setTaxStatus(organizationDTO.getTaxStatus());
        organization.setTaxId(organizationDTO.getTaxId());
        organization.setYearIncorporated(organizationDTO.getYearIncorporated());
        organization.setLegalStatus(organizationDTO.getLegalStatus());
        organization.setLogo(organizationDTO.getLogo());
        organization.setUri(organizationDTO.getUri());

        Organization updatedOrganization = this.organizationRepository.save(organization);
        return this.getOrganizationById(updatedOrganization.getId());
    }

    @Override
    public void deleteOrganization(String organizationId) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        this.attributeService.deleteRelatedAttributes(organization.getId());
        this.metadataService.deleteRelatedMetadata(organization.getId());
        this.organizationRepository.delete(organization);
    }

    @Override
    public void writeCsv(ZipOutputStream zipOutputStream) {
        try {
            // Sets CSV printer
            final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);
            // Sets CSV header
            List<String> header = Arrays.asList("id", "name", "alternate_name", "description", "email", "url", "tax_status", "tax_id", "year_incorporated", "legal_status");
            csvPrinter.printRecord(header);
            // Sets CSV entries
            for (Organization organization : organizationRepository.findAll()) {
                List<String> data = Arrays.asList(
                        organization.getId(),
                        organization.getName(),
                        organization.getAlternateName(),
                        organization.getDescription(),
                        organization.getEmail(),
                        organization.getUri(),
                        organization.getTaxStatus(),
                        organization.getTaxId(),
                        organization.getYearIncorporated() == null ? null : organization.getYearIncorporated().toString(),
                        organization.getLegalStatus()
                );
                csvPrinter.printRecord(data);
            }
            // Flushes to zip entry
            csvPrinter.flush();
            zipOutputStream.putNextEntry(new ZipEntry("organizations.csv"));
            IOUtils.copy(new ByteArrayInputStream(out.toByteArray()), zipOutputStream);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to CSV file: " + e.getMessage());
        }
    }

    @Override
    public void writePdf(ZipOutputStream zipOutputStream) {
        try {
            // Sets PDF document to write directly to zip entry stream
            zipOutputStream.putNextEntry(new ZipEntry("organizations.pdf"));
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, zipOutputStream);
            document.open();
            // Sets table
            PdfPTable table = new PdfPTable(10);
            PdfPCell cell = new PdfPCell();
            // Sets table header
            List<String> header = Arrays.asList("id", "name", "alternate_name", "description", "email", "url", "tax_status", "tax_id", "year_incorporated", "legal_status");
            header.forEach(column -> {
                cell.setPhrase(new Phrase(column));
                table.addCell(cell);
            });
            // Sets table entries
            for (Organization organization : organizationRepository.findAll()) {
                table.addCell(organization.getId());
                table.addCell(organization.getName());
                table.addCell(organization.getAlternateName());
                table.addCell(organization.getDescription());
                table.addCell(organization.getEmail());
                table.addCell(organization.getUri());
                table.addCell(organization.getTaxStatus());
                table.addCell(organization.getTaxId());
                table.addCell(organization.getYearIncorporated() == null ? null : organization.getYearIncorporated().toString());
                table.addCell(organization.getLegalStatus());
            }
            document.add(table);
            document.close();
            zipOutputStream.closeEntry();

        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to PDF file: " + e.getMessage());
        }
    }

    private void addRelatedData(OrganizationDTO organizationDTO) {
        organizationDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(organizationDTO.getId()));
        organizationDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(organizationDTO.getId()));
    }
}
