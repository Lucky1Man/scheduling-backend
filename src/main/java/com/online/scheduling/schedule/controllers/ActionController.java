package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.implementations.services.PlannedActionService;
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
    public List<PlannedAction> save(Authentication authentication, @RequestBody List<PlannedAction> plannedActions){
        return actionService.save(authentication ,plannedActions);
    }

    @GetMapping
    public List<PlannedAction> get(@RequestParam(value = "userEmail") String userEmail, @RequestParam(value = "ids", required = false) Set<Long> ids){
        return actionService.get(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(value = "ids", required = false) List<Long> ids){
        actionService.delete(ids);
    }

    @PutMapping
    public List<PlannedAction> update(Authentication authentication ,@RequestBody List<PlannedAction> actions){
        return actionService.update(authentication ,actions);
    }

}
