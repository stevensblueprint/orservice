package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Funding;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingDTO {
    private String id;
    private String source;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Funding toEntity() {
        return Funding.builder()
                .id(this.id)
                .source(this.source)
                .build();
    }
}
