package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Language;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageDTO {
    private String id;
    private String name;
    private String code;
    private String note;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Language toEntity() {
        return Language.builder()
                .id(this.id)
                .name(this.name)
                .code(this.code)
                .note(this.note)
                .build();
    }
}
