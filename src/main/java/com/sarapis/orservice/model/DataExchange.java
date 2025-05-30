package com.sarapis.orservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "data_exchange")
@Setter
@Getter
public class DataExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DataExchangeType type;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "error_message", length = 1024)
    private String errorMessage;

    @Column(name = "format")
    @Enumerated(EnumType.STRING)
    private DataExchangeFormat format;

    @Column(name = "size")
    private Long size;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "data_exchange_id", referencedColumnName = "id")
    private List<DataExchangeFile> dataExchangeFiles;
}
