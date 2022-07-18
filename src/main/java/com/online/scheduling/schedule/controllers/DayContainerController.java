package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.services.PlannedDayContainerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/v1/day-container")
public class DayContainerController {
    private final PlannedDayContainerService dayContainerService;

    public DayContainerController(
            PlannedDayContainerService dayContainerService) {
        this.dayContainerService = dayContainerService;
    }

    @PostMapping
    public List<PlannedDayContainer> save(Authentication authentication, @RequestBody List<PlannedDayContainer> dayContainer){
        return dayContainerService.save(authentication,dayContainer);
    }

    @GetMapping
    public List<PlannedDayContainer> get(@RequestParam(value = "userEmail") String userEmail, @RequestParam(name = "ids", required = false) Set<Long> ids){
        return dayContainerService.get(userEmail ,ids);
    }

    @DeleteMapping
    @ApiResponse(description =
            "Returns map: the 'key' is the id of the planned day container," +
                    "the 'value' is a list of planned days where a planned day container, with an id of a 'key', is involved")
    public Map<Long, List<PlannedDay>> delete(@RequestParam(name = "ids", required = false) List<Long> ids){
        return dayContainerService.delete(ids);
    }

    @PutMapping
    public List<PlannedDayContainer> update(@RequestBody List<PlannedDayContainer> dayContainers){
        return dayContainerService.update(dayContainers);
    }
}
