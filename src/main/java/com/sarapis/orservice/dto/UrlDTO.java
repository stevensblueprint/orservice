package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Url;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlDTO {
    private String id;
    private String label;
    private String url;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Url toEntity() {
        return Url.builder()
                .id(this.id)
                .label(this.label)
                .url(this.url)
                .build();
    }
}
