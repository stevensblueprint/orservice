package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import lombok.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organization")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organization_id")
    private List<Url> additionalWebsites = new ArrayList<>();

    // Deprecated
    @Column(name = "tax_status")
    private String taxStatus;

    // Deprecated
    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "year_incorporated")
    private int yearIncorporated;

    @Column(name = "legal_status")
    private String legalStatus;

    @Column(name = "logo")
    private String logo;

    @Column(name = "uri")
    private String uri;

    @OneToOne
    @JoinColumn(name = "parent_organization_id")
    private Organization parentOrganization = null;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organization_id")
    private List<Funding> funding = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organization_id")
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organization_id")
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organization_id")
    private List<Location> locations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "organization_id", nullable = false)
    private List<Program> programs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "organization_id", nullable = false)
    private List<OrganizationIdentifier> organizationIdentifiers = new ArrayList<>();

    public OrganizationDTO toDTO() {
        return OrganizationDTO.builder()
                .id(this.id)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .email(this.email)
                .website(this.website)
                .additionalWebsites(this.additionalWebsites.stream().map(Url::toDTO).toList())
                .taxStatus(this.taxStatus)
                .taxId(this.taxId)
                .yearIncorporated(this.yearIncorporated)
                .legalStatus(this.legalStatus)
                .logo(this.logo)
                .uri(this.uri)
                .parentOrganization(this.parentOrganization != null ? this.parentOrganization.toDTO() : null)
                .funding(this.funding.stream().map(Funding::toDTO).toList())
                .contacts(this.contacts.stream().map(Contact::toDTO).toList())
                .phones(this.phones.stream().map(Phone::toDTO).toList())
                .locations(this.locations.stream().map(Location::toDTO).toList())
                .programs(this.programs.stream().map(Program::toDTO).toList())
                .organizationIdentifiers(this.organizationIdentifiers.stream().map(OrganizationIdentifier::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }

    public static ByteArrayInputStream toCSV(List<Organization> organizations) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Organization organization : organizations) {
                List<String> data = Arrays.asList(
                    String.valueOf(organization.getId()),
                    organization.getName(),
                    organization.getDescription()
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
