package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.Status;
import com.sarapis.orservice.util.Utility;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The request body when inserting or updating a service entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertServiceDTO {
    private String name;
    private String alternateName;
    private String description;
    private String url;
    private String email;
    private Status status;
    private String interpretationServices;
    private String applicationProcess;
    private String feesDescription;
    private String waitTime;
    private String fees;
    private String accreditations;
    private String eligibilityDescription;
    private Integer minimumAge;
    private Integer maximumAge;
    private LocalDate assuredDate;
    private String assurerEmail;
    private String licenses;
    private String alert;
    private LocalDateTime lastModified;
    private String organizationId;
    private List<String> additionalUrls;
    private List<String> languages;
    private List<String> fundings;
    // TODO - Programs have an one to many relationships with services. However, for some reason, the Sarapis website
    //  allows for one service to have multiple programs. Each service can only have one program_id. Is this intended?
    //  I will temporarily set List<String> programs to String programId for now.
    // private List<String> programs;
    private String programId;
    private List<String> requiredDocuments;
    private List<String> locations;
    private List<String> phones;
    private List<String> contacts;
    private List<String> schedules;
    private List<String> serviceAreas;
    private List<UpsertCostOptionDTO> costOptions;
    private List<UpsertServiceCapacityDTO> serviceCapacities;

    public Service create() {
        return Service.builder()
                .id(UUID.randomUUID().toString())
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .url(this.url)
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
                .build();
    }

    public Service merge(Service service) {
        return Service.builder()
                .id(service.getId())
                .name(Utility.getOrDefault(this.name, service.getName()))
                .alternateName(Utility.getOrDefault(this.alternateName, service.getAlternateName()))
                .description(Utility.getOrDefault(this.description, service.getDescription()))
                .url(Utility.getOrDefault(this.url, service.getUrl()))
                .email(Utility.getOrDefault(this.email, service.getEmail()))
                .status(Utility.getOrDefault(this.status, service.getStatus()))
                .interpretationServices(Utility.getOrDefault(this.interpretationServices, service.getInterpretationServices()))
                .applicationProcess(Utility.getOrDefault(this.applicationProcess, service.getApplicationProcess()))
                .feesDescription(Utility.getOrDefault(this.feesDescription, service.getFeesDescription()))
                .waitTime(Utility.getOrDefault(this.waitTime, service.getWaitTime()))
                .fees(Utility.getOrDefault(this.fees, service.getFees()))
                .accreditations(Utility.getOrDefault(this.accreditations, service.getAccreditations()))
                .eligibilityDescription(Utility.getOrDefault(this.eligibilityDescription, service.getEligibilityDescription()))
                .minimumAge(Utility.getOrDefault(this.minimumAge, service.getMinimumAge()))
                .maximumAge(Utility.getOrDefault(this.maximumAge, service.getMaximumAge()))
                .assuredDate(Utility.getOrDefault(this.assuredDate, service.getAssuredDate()))
                .assurerEmail(Utility.getOrDefault(this.assurerEmail, service.getAssurerEmail()))
                .licenses(Utility.getOrDefault(this.licenses, service.getLicenses()))
                .alert(Utility.getOrDefault(this.alert, service.getAlert()))
                .lastModified(Utility.getOrDefault(this.lastModified, service.getLastModified()))
                .organization(service.getOrganization())
                .program(service.getProgram())
                .additionalUrls(service.getAdditionalUrls())
                .phones(service.getPhones())
                .schedules(service.getSchedules())
                .serviceAreas(service.getServiceAreas())
                .serviceAtLocations(service.getServiceAtLocations())
                .languages(service.getLanguages())
                .funding(service.getFunding())
                .costOptions(service.getCostOptions())
                .requiredDocuments(service.getRequiredDocuments())
                .contacts(service.getContacts())
                .serviceCapacities(service.getServiceCapacities())
                .build();
    }
}
