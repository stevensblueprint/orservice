package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.exception.InvalidInputException;
import com.sarapis.orservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<ScheduleDTO>> getSchedules(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                   @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
        List<ScheduleDTO> scheduleDTOs = this.scheduleService.getAllSchedules();

        if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
        if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");

        PaginationDTO<ScheduleDTO> paginationDTO = PaginationDTO.of(
            scheduleDTOs,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable String scheduleId) {
        ScheduleDTO schedule = this.scheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        ScheduleDTO createdSchedule = this.scheduleService.createSchedule(scheduleDTO);
        return ResponseEntity.ok(createdSchedule);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable String scheduleId,
                                                      @RequestBody ScheduleDTO scheduleDTO) {
        ScheduleDTO updatedSchedule = this.scheduleService.updateSchedule(scheduleId, scheduleDTO);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scheduleId) {
        this.scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
