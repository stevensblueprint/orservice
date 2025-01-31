package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
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
    public ResponseEntity<PaginationDTO<ScheduleDTO>> getSchedules(@RequestParam(defaultValue = "1") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer perPage) {
        List<ScheduleDTO> scheduleDTOs = this.scheduleService.getAllSchedules();

        try {
            PaginationDTO<ScheduleDTO> paginationDTO = PaginationDTO.of(
                    scheduleDTOs,
                    page,
                    perPage
            );
            return ResponseEntity.ok(paginationDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
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
