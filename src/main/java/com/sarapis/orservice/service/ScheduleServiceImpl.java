package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.entity.Schedule;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.exception.ResourceNotFoundException;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.repository.ScheduleRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ServiceRepository serviceRepository;
    private final LocationRepository locationRepository;
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               ServiceRepository serviceRepository,
                               LocationRepository locationRepository,
                               ServiceAtLocationRepository serviceAtLocationRepository,
                               AttributeService attributeService,
                               MetadataService metadataService) {
        this.scheduleRepository = scheduleRepository;
        this.serviceRepository = serviceRepository;
        this.locationRepository = locationRepository;
        this.serviceAtLocationRepository = serviceAtLocationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOs = this.scheduleRepository.findAll().stream().map(Schedule::toDTO).toList();
        scheduleDTOs.forEach(this::addRelatedData);
        return scheduleDTOs;
    }

    @Override
    public ScheduleDTO getScheduleById(String scheduleId) {
        Schedule schedule = this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found."));
        ScheduleDTO scheduleDTO = schedule.toDTO();
        this.addRelatedData(scheduleDTO);
        return scheduleDTO;
    }

    @Override
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        com.sarapis.orservice.entity.core.Service service = null;
        Location location = null;
        ServiceAtLocation serviceAtLocation = null;

        if (scheduleDTO.getServiceId() != null) {
            service = this.serviceRepository.findById(scheduleDTO.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found."));
        }
        if (scheduleDTO.getLocationId() != null) {
            location = this.locationRepository.findById(scheduleDTO.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found."));
        }
        if (scheduleDTO.getServiceAtLocationId() != null) {
            serviceAtLocation = this.serviceAtLocationRepository.findById(scheduleDTO.getServiceAtLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service at location not found."));
        }

        Schedule schedule = this.scheduleRepository.save(scheduleDTO.toEntity(service, location, serviceAtLocation));
        scheduleDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(schedule.getId(), attributeDTO));
        scheduleDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(schedule.getId(), e));

        Schedule createdSchedule = this.scheduleRepository.save(schedule);
        return this.getScheduleById(createdSchedule.getId());
    }

    @Override
    public ScheduleDTO updateSchedule(String scheduleId, ScheduleDTO scheduleDTO) {
        Schedule schedule = this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found."));

        schedule.setValidFrom(scheduleDTO.getValidFrom());
        schedule.setValidTo(scheduleDTO.getValidTo());
        schedule.setDtStart(schedule.getDtStart());
        schedule.setTimezone(schedule.getTimezone());
        schedule.setUntil(schedule.getUntil());
        schedule.setCount(schedule.getCount());
        schedule.setWkst(schedule.getWkst());
        schedule.setFreq(schedule.getFreq());
        schedule.setInterval(schedule.getInterval());
        schedule.setByday(schedule.getByday());
        schedule.setByweekno(schedule.getByweekno());
        schedule.setBymonthday(schedule.getBymonthday());
        schedule.setByyearday(schedule.getByyearday());
        schedule.setDescription(schedule.getDescription());
        schedule.setOpensAt(schedule.getOpensAt());
        schedule.setClosesAt(schedule.getClosesAt());
        schedule.setScheduleLink(schedule.getScheduleLink());
        schedule.setAttendingType(schedule.getAttendingType());
        schedule.setNotes(schedule.getNotes());

        Schedule updatedSchedule = this.scheduleRepository.save(schedule);
        return this.getScheduleById(updatedSchedule.getId());
    }

    @Override
    public void deleteSchedule(String scheduleId) {
        Schedule schedule = this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found."));
        this.attributeService.deleteRelatedAttributes(schedule.getId());
        this.metadataService.deleteRelatedMetadata(schedule.getId());
        this.scheduleRepository.delete(schedule);
    }

    private void addRelatedData(ScheduleDTO scheduleDTO) {
        scheduleDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(scheduleDTO.getId()));
        scheduleDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(scheduleDTO.getId()));
    }
}
