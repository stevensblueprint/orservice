package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.FundingDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "funding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funding {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "source")
    private String source;

    public FundingDTO toDTO() {
        return FundingDTO.builder()
                .id(this.id)
                .source(this.source)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
