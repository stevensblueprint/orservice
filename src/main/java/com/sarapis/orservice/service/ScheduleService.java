package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ScheduleDTO;
import java.util.List;

public interface ScheduleService {
  ScheduleDTO.Response createSchedule(ScheduleDTO.Request dto);
  List<ScheduleDTO.Response> getSchedulesByLocationId(String locationId);
  List<ScheduleDTO.Response> getSchedulesByServiceAtLocationId(String serviceAtLocationId);
}
