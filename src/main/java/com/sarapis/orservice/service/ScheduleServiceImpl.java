package com.sarapis.orservice.service;


import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.SCHEDULE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.dto.ScheduleDTO.Request;
import com.sarapis.orservice.dto.ScheduleDTO.Response;
import com.sarapis.orservice.mapper.ScheduleMapper;
import com.sarapis.orservice.model.Schedule;
import com.sarapis.orservice.repository.ScheduleRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
  private final ScheduleRepository scheduleRepository;
  private final ScheduleMapper scheduleMapper;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createSchedule(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Schedule schedule = scheduleMapper.toEntity(dto);
    Schedule savedSchedule = scheduleRepository.save(schedule);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedSchedule.getId(),
        SCHEDULE_RESOURCE_TYPE,
        CREATE.name(),
        "schedule",
        EMPTY_PREVIOUS_VALUE,
        scheduleMapper.toResponseDTO(savedSchedule).toString(),
        "SYSTEM"
    );
    ScheduleDTO.Response response = scheduleMapper.toResponseDTO(savedSchedule);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedSchedule.getId(), SCHEDULE_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getSchedulesByLocationId(String locationId) {
    List<Schedule> schedules = scheduleRepository.findByLocationId(locationId);
    List<ScheduleDTO.Response> sceduleDtos = schedules.stream().map(scheduleMapper::toResponseDTO).toList();
    sceduleDtos = sceduleDtos.stream().peek(schedule -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          schedule.getId(), SCHEDULE_RESOURCE_TYPE
      );
      schedule.setMetadata(metadata);
    }).toList();
    return sceduleDtos;
  }
}
