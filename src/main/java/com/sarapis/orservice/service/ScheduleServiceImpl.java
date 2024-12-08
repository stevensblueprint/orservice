package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.Schedule;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.scheduleRepository = scheduleRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOs = this.scheduleRepository.findAll()
                .stream()
                .map(Schedule::toDTO)
                .toList();
        scheduleDTOs.forEach(this::addRelatedData);
        return scheduleDTOs;
    }

    @Override
    public ScheduleDTO getScheduleById(String id) {
        Schedule schedule = this.scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found."));

        ScheduleDTO scheduleDTO = schedule.toDTO();
        this.addRelatedData(scheduleDTO);
        return scheduleDTO;
    }

    @Override
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = this.scheduleRepository.save(scheduleDTO.toEntity());

        for (AttributeDTO attributeDTO : scheduleDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(schedule.getId()));
        }

        for (MetadataDTO metadataDTO : scheduleDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(schedule.getId()));
        }

        ScheduleDTO savedScheduleDTO = this.scheduleRepository.save(schedule).toDTO();
        this.addRelatedData(savedScheduleDTO);
        return savedScheduleDTO;
    }

    @Override
    public ScheduleDTO updateSchedule(String id, ScheduleDTO scheduleDTO) {
        Schedule schedule = this.scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found."));

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
        return  updatedSchedule.toDTO();
    }

    @Override
    public void deleteSchedule(String id) {
        Schedule schedule = this.scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found."));

        this.scheduleRepository.deleteAttributes(schedule.getId());
        this.scheduleRepository.deleteMetadata(schedule.getId());
        this.scheduleRepository.delete(schedule);
    }

    private void addRelatedData(ScheduleDTO scheduleDTO) {
        List<AttributeDTO> attributeDTOs = this.scheduleRepository.getAttributes(scheduleDTO.getId())
                .stream()
                .map(Attribute::toDTO)
                .toList();

        List<MetadataDTO> metadataDTOs = this.scheduleRepository.getMetadata(scheduleDTO.getId())
                .stream()
                .map(Metadata::toDTO)
                .toList();
        scheduleDTO.getAttributes().addAll(attributeDTOs);
        scheduleDTO.getMetadata().addAll(metadataDTOs);
    }
}
