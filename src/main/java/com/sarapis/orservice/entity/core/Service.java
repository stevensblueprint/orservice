package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program = null;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

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

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Url> additionalUrls = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<ServiceArea> serviceAreas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "service")
    private List<ServiceAtLocation> serviceAtLocations = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Language> languages = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Funding> funding = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "service")
    private List<CostOption> costOptions = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<RequiredDocument> requiredDocuments = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "service")
    private List<ServiceCapacity> capacities = new ArrayList<>();

    @PreRemove
    public void preRemove() {
        // Sets optional foreign keys to null since we're not using CascadeType.ALL
        for (Url url : additionalUrls) {
            url.setService(null);
        }
        for (Phone phone : phones) {
            phone.setService(null);
        }
        for (Schedule schedule : schedules) {
            schedule.setService(null);
        }
        for (ServiceArea serviceArea : serviceAreas) {
            serviceArea.setService(null);
        }
        for (Language language : languages) {
            language.setService(null);
        }
        for (RequiredDocument requiredDocument : requiredDocuments) {
            requiredDocument.setService(null);
        }
        for (Contact contact : contacts) {
            contact.setService(null);
        }
    }

    public ServiceDTO toDTO() {
        return ServiceDTO.builder()
                .id(this.id)
                .organizationId(this.organization.getId())
                .programId(this.program == null ? null : this.program.getId())
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
                .funding(this.funding.stream().map(Funding::toDTO).toList())
                .costOptions(this.costOptions.stream().map(CostOption::toDTO).toList())
                .requiredDocuments(this.requiredDocuments.stream().map(RequiredDocument::toDTO).toList())
                .contacts(this.contacts.stream().map(Contact::toDTO).toList())
                .capacities(this.capacities.stream().map(ServiceCapacity::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
