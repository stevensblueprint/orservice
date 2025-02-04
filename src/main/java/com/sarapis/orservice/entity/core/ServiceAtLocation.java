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
        this.service.getServiceAtLocations().remove(this);
        this.location.getServiceAtLocations().remove(this);
        this.serviceAreas.forEach(serviceArea -> serviceArea.setServiceAtLocation(this));
        this.contacts.forEach(contact -> contact.setServiceAtLocation(this));
        this.phones.forEach(phone -> phone.setServiceAtLocation(this));
        this.schedules.forEach(schedule -> schedule.setServiceAtLocation(this));
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
                .location(this.location == null ? null : this.location.toDTO())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
