package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Phone;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDTO {
    private String id;
    private String number;
    private String extension;
    private String type;
    private String description;
    private List<LanguageDTO> languages = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Phone toEntity() {
        return Phone.builder()
                .id(this.id)
                .number(this.number)
                .extension(this.extension)
                .type(this.type)
                .description(this.description)
                .languages(this.languages.stream().map(LanguageDTO::toEntity).toList())
                .build();
    }
}
