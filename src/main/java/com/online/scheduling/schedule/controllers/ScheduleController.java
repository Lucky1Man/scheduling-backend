package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.implementations.services.ScheduleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public void save(Authentication authentication, @RequestBody List<Schedule> schedules){
        scheduleService.saveAll(authentication ,schedules);
    }

    @GetMapping
    public List<Schedule> get(@RequestParam(value = "userEmail") String userEmail,
                              @RequestParam(name = "ids", required = false) Set<Long> ids){
        return scheduleService.getSchedules(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "ids", required = false) List<Long> ids){
        scheduleService.delete(ids);
    }

    @PutMapping
    public void update(Authentication authentication ,@RequestBody List<Schedule> schedules){
        scheduleService.update(authentication ,schedules);
    }

}
