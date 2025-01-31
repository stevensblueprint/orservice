package com.sarapis.orservice.dto.upsert;

import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.Status;
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
                .name(name)
                .alternateName(alternateName)
                .description(description)
                .url(url)
                .email(email)
                .status(status)
                .interpretationServices(interpretationServices)
                .applicationProcess(applicationProcess)
                .feesDescription(feesDescription)
                .waitTime(waitTime)
                .fees(fees)
                .accreditations(accreditations)
                .eligibilityDescription(eligibilityDescription)
                .minimumAge(minimumAge)
                .maximumAge(maximumAge)
                .assuredDate(assuredDate)
                .assurerEmail(assurerEmail)
                .licenses(licenses)
                .alert(alert)
                .lastModified(lastModified)
                .build();
    }

    public Service merge(Service service) {
        return Service.builder()
                .name(name == null ? service.getName() : name)
                .alternateName(alternateName == null ? service.getAlternateName() : alternateName)
                .description(description == null ? service.getDescription() : description)
                .url(url == null ? service.getUrl() : url)
                .email(email == null ? service.getEmail() : email)
                .status(status == null ? service.getStatus() : status)
                .interpretationServices(interpretationServices == null ? service.getInterpretationServices() : interpretationServices)
                .applicationProcess(applicationProcess == null ? service.getApplicationProcess() : applicationProcess)
                .feesDescription(feesDescription == null ? service.getFeesDescription() : feesDescription)
                .waitTime(waitTime == null ? service.getWaitTime() : waitTime)
                .fees(fees == null ? service.getFees() : fees)
                .accreditations(accreditations == null ? service.getAccreditations() : accreditations)
                .eligibilityDescription(eligibilityDescription == null ? service.getEligibilityDescription() : eligibilityDescription)
                .minimumAge(minimumAge == null ? service.getMinimumAge() : minimumAge)
                .maximumAge(maximumAge == null ? service.getMaximumAge() : maximumAge)
                .assuredDate(assuredDate == null ? service.getAssuredDate() : assuredDate)
                .assurerEmail(assurerEmail == null ? null : assurerEmail)
                .licenses(licenses == null ? service.getLicenses() : licenses)
                .alert(alert == null ? service.getAlert() : alert)
                .lastModified(lastModified == null ? service.getLastModified() : lastModified)
                .build();
    }
}
