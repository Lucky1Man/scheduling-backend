package com.online.scheduling.schedule.models.schedule;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.carriedoutfunctions.CheckIfUpdatingStuffNeedsToBeAddedToItsOwner;
import com.online.scheduling.schedule.carriedoutfunctions.RemoveMarkedPlannedStuffFromItsOwner;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.enums.ScheduleValidatorComponentsIds;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.schedule.ScheduleValidator;
import com.online.scheduling.user.entities.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.online.scheduling.schedule.enums.PlannedStuffRequestsPairedComponents.NAME_VALIDATION_VALIDATOR_CONFIG;
import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;
import static com.online.scheduling.schedule.enums.ScheduleValidatorComponentsIds.S_OWNER_VALIDATOR_COMPONENT;

public class ClearUpdatingScheduleRequest {
    @Getter
    private List<Schedule> schedules;
    private final ScheduleValidator scheduleValidator;
    @Getter
    private List<List<PlannedDay>> idsOfDaysToBeRemovedFromSchedules;
    @Getter
    private List<PlannedDay> plannedDaysToSave = new ArrayList<>();
    @Getter
    private final List<PlannedDay> plannedDaysToUpdate = new ArrayList<>();
    @Getter
    private List<ScheduleContainer> scheduleContainersToSave = new ArrayList<>();
    @Getter
    private final List<ScheduleContainer> scheduleContainersToUpdate = new ArrayList<>();

    public ClearUpdatingScheduleRequest(List<Schedule> schedules) {
        this.schedules = schedules;
        scheduleValidator = SchedulingProvider.getValidator().getScheduleValidator();
        init();
    }

    private void init(){
        //#1 initializes idsOfDaysToBeRemovedFromSchedules
        initIdsOfDaysToBeRemovedFromSchedules();
        //#2 fills idsOfDaysToBeRemovedFromSchedules with stuff to be removed
        // NOTE: validateAllIgnoringNulls must be called before #3
        validateAllIgnoringNulls();
        //#3 removes all marked stuff from its owner depending on witten things to idsOfDaysToBeRemovedFromSchedules
        schedules = RemoveMarkedPlannedStuffFromItsOwner.removeMarkedPlannedStuffFromItsOwner(schedules,idsOfDaysToBeRemovedFromSchedules);
        plannedDaysToSave = SchedulingProvider.getInitializer().getDayInitializer().doInit(plannedDaysToSave,schedules);
        scheduleContainersToSave = SchedulingProvider.getInitializer().getScheduleContainerInitializer().doInit(scheduleContainersToSave, schedules);
    }
    public List<PlannedDay> getDaysToBeAdded(){
        ArrayList<PlannedDay> plannedDays = new ArrayList<>(plannedDaysToSave);
        plannedDays.addAll(
                CheckIfUpdatingStuffNeedsToBeAddedToItsOwner.getUpdatingPlannedStuffToBeAdded(
                        plannedDaysToUpdate,
                        schedules,
                        SchedulingProvider.getRepositories().getScheduleRepository()));
        return plannedDays;
    }

    private void removeMarkedPlannedDaysFromSchedules() {
        int i = 0;
        for(var schedule : schedules){
            var plannedDaysToRemove = idsOfDaysToBeRemovedFromSchedules.get(i);
            for(int j = 0; j < schedule.getDays().size(); j++){
                var days = schedule.getDays().get(j);
                for(var dayToRem : plannedDaysToRemove){
                    if(days.getId().equals(dayToRem.getId()))
                        schedule.getDays().remove(j);
                }
            }
            i++;
        }
    }
    private void initIdsOfDaysToBeRemovedFromSchedules(){
        idsOfDaysToBeRemovedFromSchedules = new ArrayList<>();
        for(var schedule : schedules){
            idsOfDaysToBeRemovedFromSchedules.add(new ArrayList<>());
        }
    }

    private void validateAllIgnoringNulls(){
        int i = 0;
        for(var schedule : schedules){
            Long scheduleId = schedule.getId();
            if(scheduleId == null)
                throw new UnableToExecuteQueryException(String.format("Updating schedule {%s} can not have empty id", schedule));
            scheduleValidator.validateUsing(schedule, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
            if(schedule.getName() != null)
                scheduleValidator.validateUsing(schedule, NAME_VALIDATION_VALIDATOR_CONFIG.getComponentsIds());
            var plannedDays = schedule.getDays();
            if(plannedDays != null && !plannedDays.isEmpty()){
                for(var day : plannedDays){
                    Long dayId = day.getId();
                    if(dayId != null && -dayId > 0){
                        day.setId(-dayId);
                        idsOfDaysToBeRemovedFromSchedules.get(i).add(day);
                        continue;
                    }
                    if(dayId != null)
                        plannedDaysToUpdate.add(day);
                }
            }
            i++;
            var scheduleContainer = schedule.getScheduleContainer();
            if(scheduleContainer != null && scheduleContainer.getId() != null)
                scheduleContainersToUpdate.add(scheduleContainer);
//            User owner = schedule.getOwner();
//            if(owner != null)
//                scheduleValidator.validateUsing(schedule, List.of(S_OWNER_VALIDATOR_COMPONENT.getId()));
        }
    }
}
