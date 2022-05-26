package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.services.ScheduleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedule-management")
public class SchedulingController {

    private final ScheduleManagementService scheduleManagementService;

    @Autowired
    public SchedulingController(
            @Qualifier("SchedulingManagementService")
            ScheduleManagementService scheduleManagementService) {
        this.scheduleManagementService = scheduleManagementService;
    }

    @PostMapping
    public void saveSchedule(){
        scheduleManagementService.saveSchedule();
    }
}
