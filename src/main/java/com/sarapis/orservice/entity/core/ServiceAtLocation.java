package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.Schedule;
import com.sarapis.orservice.entity.ServiceArea;
import jakarta.persistence.*;
import lombok.*;

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
    //================================================================================
    // Attributes
    //================================================================================

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "description")
    private String description;

    //================================================================================
    // Relations
    //================================================================================

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "serviceAtLocation")
    private List<ServiceArea> serviceAreas;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "serviceAtLocation")
    private List<Contact> contacts;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "serviceAtLocation")
    private List<Phone> phones;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "serviceAtLocation")
    private List<Schedule> schedules;

    //================================================================================
    // Methods
    //================================================================================

    @PreRemove
    public void preRemove() {
        // Sets optional foreign keys to null since we're not using CascadeType.ALL
        for (ServiceArea serviceArea : serviceAreas) {
            serviceArea.setServiceAtLocation(null);
        }
        for (Contact contact : contacts) {
            contact.setServiceAtLocation(null);
        }
        for (Phone phone : phones) {
            phone.setServiceAtLocation(null);
        }
        for (Schedule schedule : schedules) {
            schedule.setServiceAtLocation(null);
        }
    }

    public ServiceAtLocationDTO toDTO() {
        return ServiceAtLocationDTO.builder()
                .id(this.id)
                .serviceId(this.service.getId())
                .locationId(this.location.getId())
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
