package com.sarapis.orservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {
    @NotNull
    private String id;

    @NotBlank
    private String locationId;

    private String serviceId;
    private String organizationId;
    private String contactId;
    private String serviceAtLocationId;
    private String number;
    private String extension;
    private String type;
    private String description;
    private List<LanguageDTO> languages;
    private List<Object> attributes;
    private List<Object> metadata;
}
