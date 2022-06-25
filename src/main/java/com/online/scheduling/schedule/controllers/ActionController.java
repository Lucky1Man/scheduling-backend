package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.implementations.services.PlannedActionContainerService;
import com.online.scheduling.schedule.implementations.services.PlannedActionService;
import com.online.scheduling.schedule.models.plannedaction.ClearDeletingActionRequest;
import com.online.scheduling.schedule.models.plannedaction.ClearSavingActionRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/action")
public class ActionController {
    private final PlannedActionService actionService;

    public ActionController(
            PlannedActionService actionService) {
        this.actionService = actionService;
    }

    @PostMapping
    public void save(Authentication authentication, @RequestBody List<PlannedAction> plannedActions){
        actionService.saveAll(authentication ,plannedActions);
    }

    @GetMapping
    public List<PlannedAction> get(@RequestParam(value = "userEmail") String userEmail,
                                   @RequestParam(value = "ids", required = false) Set<Long> ids){
        return actionService.getActions(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(value = "ids", required = false) List<Long> ids){
        actionService.delete(ids);
    }

    @PutMapping
    public void update(Authentication authentication ,@RequestBody List<PlannedAction> actions){
        actionService.update(authentication ,actions);
    }

}
