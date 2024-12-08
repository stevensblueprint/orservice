package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.RequiredDocumentDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "required_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredDocument {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "document")
    private String document;

    @Column(name = "uri")
    private String uri;

    public RequiredDocumentDTO toDTO() {
        return RequiredDocumentDTO.builder()
                .id(this.id)
                .document(this.document)
                .uri(this.uri)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
