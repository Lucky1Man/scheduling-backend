package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.services.PlannedActionContainerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/action-container")
public class ActionContainerController {
    private final PlannedActionContainerService actionContainerService;

    public ActionContainerController(
            PlannedActionContainerService actionContainerService) {
        this.actionContainerService = actionContainerService;
    }

    @PostMapping
    public List<PlannedActionContainer> save(Authentication authentication ,@RequestBody List<PlannedActionContainer> actionContainers){
        return actionContainerService.save(authentication ,actionContainers);
    }

    @GetMapping
    public List<PlannedActionContainer> get(@RequestParam(value = "userEmail") String userEmail,@RequestParam(value = "ids", required = false) Set<Long> ids){
        return actionContainerService.get(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(value = "ids", required = false) List<Long> ids){
        actionContainerService.delete(ids);
    }

    @PutMapping
    public List<PlannedActionContainer> update(@RequestBody List<PlannedActionContainer> actionContainers){
        return actionContainerService.update(actionContainers);
    }
}
