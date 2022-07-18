package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.services.PlannedActionContainerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    @ResponseStatus(HttpStatus.CREATED)
    public List<PlannedActionContainer> save(Authentication authentication, @RequestBody List<PlannedActionContainer> actionContainers) {
        return actionContainerService.save(authentication, actionContainers);
    }

    @GetMapping
    public List<PlannedActionContainer> get(@RequestParam(value = "userEmail") String userEmail, @RequestParam(value = "ids", required = false) Set<Long> ids) {
        return actionContainerService.get(userEmail, ids);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(description =
                    "Returns map: the 'key' is the id of the planned action container," +
                    "the 'value' is a list of planned actions where a planned action container, with an id of a 'key', is involved")
    public Map<Long, List<PlannedAction>> delete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        return actionContainerService.delete(ids);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlannedActionContainer> update(@RequestBody List<PlannedActionContainer> actionContainers) {
        return actionContainerService.update(actionContainers);
    }
}
