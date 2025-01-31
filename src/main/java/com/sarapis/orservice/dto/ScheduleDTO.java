package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Freq;
import com.sarapis.orservice.entity.Schedule;
import com.sarapis.orservice.entity.WkSt;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO {
    private String id;

    private String serviceId;
    private String locationId;
    private String serviceAtLocationId;

    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDate dtStart;
    private int timezone;
    private LocalDate until;
    private int count;
    private WkSt wkst;
    private Freq freq;
    private int interval;
    private String byday;
    private String byweekno;
    private String bymonthday;
    private String byyearday;
    private String description;
    private LocalTime opensAt;
    private LocalTime closesAt;
    private String scheduleLink;
    private String attendingType;
    private String notes;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Schedule toEntity(Service service, Location location, ServiceAtLocation serviceAtLocation) {
        return Schedule.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .service(service)
                .location(location)
                .serviceAtLocation(serviceAtLocation)
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .dtStart(this.dtStart)
                .timezone(this.timezone).until(this.until)
                .count(this.count)
                .wkst(this.wkst)
                .freq(this.freq)
                .interval(this.interval)
                .byday(this.byday)
                .byweekno(this.byweekno)
                .bymonthday(this.bymonthday)
                .byyearday(this.byyearday)
                .description(this.description)
                .opensAt(this.opensAt)
                .closesAt(this.closesAt)
                .scheduleLink(this.scheduleLink)
                .attendingType(this.attendingType)
                .notes(this.notes)
                .build();
    }
}
