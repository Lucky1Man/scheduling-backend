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
    public List<Schedule> save(Authentication authentication, @RequestBody List<Schedule> schedules){
        return scheduleService.save(authentication ,schedules);
    }

    @GetMapping
    public List<Schedule> get(@RequestParam(value = "userEmail") String userEmail,
                              @RequestParam(name = "ids", required = false) Set<Long> ids){
        return scheduleService.get(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "ids", required = false) List<Long> ids){
        scheduleService.delete(ids);
    }

    @PutMapping
    public List<Schedule> update(Authentication authentication ,@RequestBody List<Schedule> schedules){
        return scheduleService.update(authentication ,schedules);
    }

}
