package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> getAllSchedules();

    ScheduleDTO getScheduleById(String scheduleId);

    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO updateSchedule(String scheduleId, ScheduleDTO scheduleDTO);

    void deleteSchedule(String scheduleId);
}
