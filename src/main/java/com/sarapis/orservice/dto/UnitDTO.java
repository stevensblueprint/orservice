package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Unit;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitDTO {
    private String id;

    private String name;
    private String scheme;
    private String identifier;
    private String uri;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Unit toEntity() {
        return Unit.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .name(this.name)
                .scheme(this.scheme)
                .identifier(this.identifier)
                .uri(this.uri)
                .build();
    }
}
