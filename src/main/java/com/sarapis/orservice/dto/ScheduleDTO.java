package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Freq;
import com.sarapis.orservice.entity.Schedule;
import com.sarapis.orservice.entity.WkSt;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO {
    private String id;
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

    public Schedule toEntity() {
        return Schedule.builder()
                .id(this.id)
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .dtStart(this.dtStart)
                .timezone(this.timezone)
                .until(this.until)
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
                .attributes(this.attributes.stream().map(AttributeDTO::toEntity).toList())
                .metadata(this.metadata.stream().map(MetadataDTO::toEntity).toList())
                .build();
    }
}
