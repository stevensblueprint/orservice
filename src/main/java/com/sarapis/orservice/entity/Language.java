package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "phone_id")
    private Phone phone;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "note")
    private String note;

    public LanguageDTO toDTO() {
        return LanguageDTO.builder()
                .id(this.id)
                .serviceId(this.service == null ? null : this.service.getId())
                .locationId(this.location == null ? null : this.location.getId())
                .phoneId(this.phone == null ? null : this.phone.getId())
                .name(this.name)
                .code(this.code)
                .note(this.note)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
