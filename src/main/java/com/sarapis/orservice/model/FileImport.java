package com.sarapis.orservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "file_import")
@Setter
@Getter
public class FileImport {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "size")
    private Long size;

    @Column(name = "data_exchange_id")
    private String exchangeId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_import_id", referencedColumnName = "id")
    private List<Metadata> metadata;
}
