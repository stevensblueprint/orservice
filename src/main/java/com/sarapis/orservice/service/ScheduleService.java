package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> getAllSchedules();

    ScheduleDTO getScheduleById(String id);

    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO updateSchedule(String id, ScheduleDTO scheduleDTO);

    void deleteSchedule(String id);
}
