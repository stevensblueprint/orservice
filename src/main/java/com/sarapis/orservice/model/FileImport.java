package com.sarapis.orservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "file_import")
@Setter
@Getter
public class FileImport {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "exchange_id")
    private String exchangeId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_import_id", referencedColumnName = "id")
    private List<Metadata> metadata;
}
