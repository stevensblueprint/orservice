package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getSchedules() {
        List<ScheduleDTO> scheduleDTOs = this.scheduleService.getAllSchedules();
        return ResponseEntity.ok(scheduleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable String id) {
        ScheduleDTO scheduleDTO = this.scheduleService.getScheduleById(id);
        return ResponseEntity.ok(scheduleDTO);
    }

    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        if(scheduleDTO.getId() == null) {
            scheduleDTO.setId(UUID.randomUUID().toString());
        }
        ScheduleDTO createdScheduleDTO = this.scheduleService.createSchedule(scheduleDTO);
        return ResponseEntity.ok(createdScheduleDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable String id, @RequestBody ScheduleDTO scheduleDTO) {
        ScheduleDTO updatedScheduleDTO = this.scheduleService.updateSchedule(id, scheduleDTO);
        return ResponseEntity.ok(updatedScheduleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
        this.scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
