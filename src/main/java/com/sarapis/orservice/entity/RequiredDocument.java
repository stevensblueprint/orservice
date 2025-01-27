package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.RequiredDocumentDTO;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "document")
    private String document;

    @Column(name = "uri")
    private String uri;

    public RequiredDocumentDTO toDTO() {
        return RequiredDocumentDTO.builder()
                .id(this.id)
                .serviceId(this.service == null ? null : this.service.getId())
                .document(this.document)
                .uri(this.uri)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
