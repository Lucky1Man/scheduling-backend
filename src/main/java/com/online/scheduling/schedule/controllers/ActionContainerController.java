package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.services.PlannedActionContainerService;
import com.online.scheduling.security.authentication.models.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public void save(Authentication authentication ,@RequestBody List<PlannedActionContainer> actionContainers){
        actionContainerService.saveAll(authentication ,actionContainers);
    }

    @GetMapping
    public List<PlannedActionContainer> get(@RequestParam(value = "userEmail") String userEmail,@RequestParam(value = "ids", required = false) Set<Long> ids){
        return actionContainerService.getActionContainers(userEmail ,ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(value = "ids", required = false) List<Long> ids){
        actionContainerService.delete(ids);
    }

    @PutMapping
    public void update(@RequestBody List<PlannedActionContainer> actionContainers){
        actionContainerService.updateAll(actionContainers);
    }
}
