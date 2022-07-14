package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.services.ScheduleContainerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/schedule-container")
public class ScheduleContainerController {
    private final ScheduleContainerService containerService;

    public ScheduleContainerController(
            ScheduleContainerService containerService) {
        this.containerService = containerService;
    }

    @PostMapping
    public List<ScheduleContainer> save(Authentication authentication, @RequestBody List<ScheduleContainer> scheduleContainers){
        return containerService.save(authentication, scheduleContainers);
    }

    @GetMapping
    public List<ScheduleContainer> get(@RequestParam(value = "userEmail") String userEmail,
                                       @RequestParam(name = "ids", required = false) Set<Long> ids){
        return containerService.get(userEmail , ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "ids", required = false) List<Long> ids){
        containerService.delete(ids);
    }

    @PutMapping
    public List<ScheduleContainer> update(@RequestBody List<ScheduleContainer> scheduleContainers){
        return containerService.update(scheduleContainers);
    }
}
