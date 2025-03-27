package com.sarapis.orservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "exchange")
@Setter
@Getter
public class Exchange {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ExchangeType type;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "format")
    @Enumerated(EnumType.STRING)
    private ExchangeFormat format;

    @Column(name = "size")
    private Long size;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "exchange_id", referencedColumnName = "id")
    private List<FileImport> fileImports;
}
