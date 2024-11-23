package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<Url> additionalUrls = new ArrayList<>();

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "interpretation_services")
    private String interpretationServices;

    @Column(name = "application_process")
    private String applicationProcess;

    @Column(name = "fees_description")
    private String feesDescription;

    // Deprecated
    @Column(name = "wait_time")
    private String waitTime;

    // Deprecated
    @Column(name = "fees")
    private String fees;

    @Column(name = "accreditations")
    private String accreditations;

    @Column(name = "eligibility_description")
    private String eligibilityDescription;

    @Column(name = "minimum_age")
    private int minimumAge;

    @Column(name = "maximum_age")
    private int maximumAge;

    @Column(name = "assured_date")
    private LocalDate assuredDate;

    @Column(name = "assurer_email")
    private String assurerEmail;

    // Deprecated
    @Column(name = "licenses")
    private String licenses;

    @Column(name = "alert")
    private String alert;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<Phone> phones = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<ServiceArea> serviceAreas = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<ServiceAtLocation> serviceAtLocations = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<Language> languages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization = null;

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<Funding> funding = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<CostOption> costOptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program = null;

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<RequiredDocument> requiredDocuments = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_id")
    private List<ServiceCapacity> capacities = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "link_id")
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();

    public ServiceDTO toDTO() {
        return ServiceDTO.builder()
                .id(this.id)
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .url(this.url)
                .additionalUrls(this.additionalUrls.stream().map(Url::toDTO).toList())
                .email(this.email)
                .status(this.status)
                .interpretationServices(this.interpretationServices)
                .applicationProcess(this.applicationProcess)
                .feesDescription(this.feesDescription)
                .waitTime(this.waitTime)
                .fees(this.fees)
                .accreditations(this.accreditations)
                .eligibilityDescription(this.eligibilityDescription)
                .minimumAge(this.minimumAge)
                .maximumAge(this.maximumAge)
                .assuredDate(this.assuredDate)
                .assurerEmail(this.assurerEmail)
                .licenses(this.licenses)
                .alert(this.alert)
                .lastModified(this.lastModified)
                .phones(this.phones.stream().map(Phone::toDTO).toList())
                .schedules(this.schedules.stream().map(Schedule::toDTO).toList())
                .serviceAreas(this.serviceAreas.stream().map(ServiceArea::toDTO).toList())
                .serviceAtLocations(this.serviceAtLocations.stream().map(ServiceAtLocation::toDTO).toList())
                .languages(this.languages.stream().map(Language::toDTO).toList())
                .organization(this.organization != null ? organization.toDTO() : null)
                .funding(this.funding.stream().map(Funding::toDTO).toList())
                .costOptions(this.costOptions.stream().map(CostOption::toDTO).toList())
                .program(this.program != null ? program.toDTO() : null)
                .requiredDocuments(this.requiredDocuments.stream().map(RequiredDocument::toDTO).toList())
                .contacts(this.contacts.stream().map(Contact::toDTO).toList())
                .capacities(this.capacities.stream().map(ServiceCapacity::toDTO).toList())
                .attributes(this.attributes.stream().map(Attribute::toDTO).toList())
                .metadata(this.metadata.stream().map(Metadata::toDTO).toList())
                .build();
    }
}
