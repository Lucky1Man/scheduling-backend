package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.implementations.services.PlannedDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/day")
public class DaysController {

    private final PlannedDayService dayService;

    @Autowired
    public DaysController(
            PlannedDayService dayService) {
        this.dayService = dayService;
    }

    @PostMapping
    public List<PlannedDay> save(Authentication authentication, @RequestBody List<PlannedDay> plannedDays) throws RuntimeException {
        return dayService.save(authentication,plannedDays);
    }

    @GetMapping
    public List<PlannedDay> get(@RequestParam(value = "userEmail") String userEmail, @RequestParam(name = "ids", required = false) Set<Long> ids){
        return dayService.get(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "ids", required = false) List<Long> ids){
        dayService.delete(ids);
    }

    @PutMapping
    public List<PlannedDay> update(Authentication authentication , @RequestBody List<PlannedDay> plannedDays){
        return dayService.update(authentication,plannedDays);
    }

}
