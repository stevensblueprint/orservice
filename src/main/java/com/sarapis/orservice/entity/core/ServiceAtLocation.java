package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.Schedule;
import com.sarapis.orservice.entity.ServiceArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_at_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAtLocation {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "service_at_location_id")
    private List<ServiceArea> serviceAreas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "service_at_location_id")
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "service_at_location_id")
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "service_at_location_id")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public ServiceAtLocationDTO toDTO() {
        return ServiceAtLocationDTO.builder()
                .id(this.id)
                .description(this.description)
                .serviceAreas(this.serviceAreas.stream().map(ServiceArea::toDTO).toList())
                .contacts(this.contacts.stream().map(Contact::toDTO).toList())
                .phones(this.phones.stream().map(Phone::toDTO).toList())
                .schedules(this.schedules.stream().map(Schedule::toDTO).toList())
                .location(this.location != null ? this.location.toDTO() : null)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
