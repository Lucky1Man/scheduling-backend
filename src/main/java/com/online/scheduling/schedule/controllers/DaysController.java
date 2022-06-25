package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.implementations.services.PlannedDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    public void save(Authentication authentication,
                     @RequestBody List<PlannedDay> plannedDays) throws RuntimeException {
        dayService.saveAll(authentication,plannedDays);
    }

    @GetMapping
    public List<PlannedDay> get(@RequestParam(value = "userEmail") String userEmail,
                                @RequestParam(name = "ids", required = false) Set<Long> ids){
        return dayService.getDays(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "ids", required = false) List<Long> ids){
        dayService.delete(ids);
    }

    @PutMapping
    public void update(Authentication authentication ,@RequestBody List<PlannedDay> plannedDays){
        dayService.update(authentication,plannedDays);
    }

}
