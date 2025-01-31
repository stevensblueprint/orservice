package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.*;

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
    //================================================================================
    // Attributes
    //================================================================================

    @Id
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
    private Integer minimumAge;

    @Column(name = "maximum_age")
    private Integer maximumAge;

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

    //================================================================================
    // Relations
    //================================================================================

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Url> additionalUrls;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Phone> phones;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Schedule> schedules;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<ServiceArea> serviceAreas;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "service")
    private List<ServiceAtLocation> serviceAtLocations;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Language> languages;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Funding> funding;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "service")
    private List<CostOption> costOptions;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<RequiredDocument> requiredDocuments;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "service")
    private List<Contact> contacts;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "service")
    private List<ServiceCapacity> capacities;

    //================================================================================
    // Methods
    //================================================================================

    @PreRemove
    public void preRemove() {
        this.organization.getServices().remove(this);
        if (this.program != null) {
            this.program.getServices().remove(this);
        }
        for (Url url : this.additionalUrls) {
            url.setService(null);
        }
        for (Phone phone : this.phones) {
            phone.setService(null);
        }
        for (Schedule schedule : this.schedules) {
            schedule.setService(null);
        }
        for (ServiceArea serviceArea : this.serviceAreas) {
            serviceArea.setService(null);
        }
        for (ServiceAtLocation serviceAtLocation : this.serviceAtLocations) {
            serviceAtLocation.setService(null);
        }
        for (Language language : this.languages) {
            language.setService(null);
        }
        for (Funding funding : this.funding) {
            funding.setService(null);
        }
        for (CostOption costOption : this.costOptions) {
            costOption.setService(null);
        }
        for (RequiredDocument requiredDocument : this.requiredDocuments) {
            requiredDocument.setService(null);
        }
        for (Contact contact : this.contacts) {
            contact.setService(null);
        }
        for (ServiceCapacity capacity : this.capacities) {
            capacity.setService(null);
        }
    }

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
                .organizationId(this.organization.getId())
                .funding(this.funding.stream().map(Funding::toDTO).toList())
                .costOptions(this.costOptions.stream().map(CostOption::toDTO).toList())
                .programId(this.program == null ? null : this.program.getId())
                .requiredDocuments(this.requiredDocuments.stream().map(RequiredDocument::toDTO).toList())
                .contacts(this.contacts.stream().map(Contact::toDTO).toList())
                .capacities(this.capacities.stream().map(ServiceCapacity::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
