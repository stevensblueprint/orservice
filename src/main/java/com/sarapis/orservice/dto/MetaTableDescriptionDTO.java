package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.MetaTableDescription;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaTableDescriptionDTO {
    private String id;
    private String name;
    private String language;
    private String characterSet;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public MetaTableDescription toEntity() {
        return MetaTableDescription.builder()
                .id(this.id)
                .name(this.name)
                .language(this.language)
                .characterSet(this.characterSet)
                .build();
    }
}
