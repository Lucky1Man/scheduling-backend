package com.online.scheduling.schedule.models.excluded;

import com.online.scheduling.exceptions.InitializingOfClearRequestObjectException;
import com.online.scheduling.exceptions.ValidationExceptionGivenUnexistingId;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.initializers.ScheduleInitializer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.ScheduleValidator;
import lombok.Getter;

import java.util.List;


@Getter
public class ClearSavingDaysRequest {
    //////////////current highest possible request//////////////////
    private List<PlannedDay> plannedDays;
    ////////////////////////////////////////////////////////////////

    //////////////////planned stuff containers//////////////////////
    private List<PlannedActionContainer> plannedActionContainers;
    private List<PlannedDayContainer> plannedDayContainers;
    ////////////////////////////////////////////////////////////////

    //////////////////////////planned stuff/////////////////////////
    private List<PlannedAction> plannedActions;
    ////////////////////////////////////////////////////////////////

    private final ScheduleValidator scheduleValidator;
    private final ScheduleInitializer scheduleInitializer;

    public ClearSavingDaysRequest(
            CreateScheduleRequest scheduleRequest) throws RuntimeException {
        this.plannedDays = scheduleRequest.getPlannedDays();
        this.scheduleValidator = SchedulingProvider.getValidator();
        this.scheduleInitializer = SchedulingProvider.getInitializer();
        this.init();
    }

    private void init() throws ValidationExceptionGivenUnexistingId{
        initPlannedDays();

        plannedDayContainers = scheduleInitializer.getDayContainerInitializer()
                .doInit(plannedDayContainers, plannedDays);
//        initPlannedDayContainers();

        plannedActions = scheduleInitializer.getActionInitializer()
                .doInit(plannedActions,plannedDays);

        plannedActionContainers = scheduleInitializer.getActionContainerInitializer()
                .doInit(plannedActionContainers ,plannedActions);

//        initPlannedActionContainers();
    }
//    private void initPlannedActionContainers() throws RuntimeException{
//        plannedActionContainers = new LinkedList<>();
//        for(var action : plannedActions){
//            PlannedActionContainer actionContainer = action.getPlannedActionContainer();
//            if(actionContainer != null && scheduleValidator.getActionContainerValidator().validateAll(actionContainer)){
//                plannedActionContainers.add(actionContainer);
//            }
//        }
//    }
//
//    private void initPlannedDayContainers() throws RuntimeException{
//        plannedDayContainers = new LinkedList<>();
//        for(var day : plannedDays){
//            PlannedDayContainer plannedDayContainer = day.getPlannedDayContainer();
//            if(plannedDayContainer != null && scheduleValidator.getDayContainerValidator().validateAll(plannedDayContainer)){
//                plannedDayContainers.add(plannedDayContainer);
//            }
//        }
//    }
//    private void initPlannedActions() throws RuntimeException{
//        plannedActions = new LinkedList<>();
//        for(var day : plannedDays){
//            List<PlannedAction> plannedActions1 = day.getPlannedActions();
//            for(var action : plannedActions1){
//                if(scheduleValidator.getActionValidator().validateAll(action)){
//                    plannedActions.add(action);
//                }
//            }
//        }
//    }
    private void initPlannedDays() throws RuntimeException{
        if(plannedDays == null)
            throw new InitializingOfClearRequestObjectException("Scheduling request should include at least one day");
        for(int i = 0; i < plannedDays.size(); i++){
//            if(!scheduleValidator.getDayValidator().validateAll(plannedDays.get(i))){
//                plannedDays.remove(i);
//                i--;
//            }
        }
        searchForDuplicatedPlannedActionsInDay();
        searchForDuplicatedDays();
        manageDuplicatedActionsInDifferentDays();
    }

    private void searchForDuplicatedDays(){
        for(var current : plannedDays){
            for(var iterator : plannedDays){
                if(current != iterator && current.getName().equals(iterator.getName())){
                    if(current.equals(iterator)){
                        throw new InitializingOfClearRequestObjectException("Saving request can not have duplicated days");
                    }
                }
            }
        }
    }
    private void searchForDuplicatedPlannedActionsInDay(){
        for(var day : plannedDays){
            for(var current : day.getPlannedActions()){
                for(var searching : day.getPlannedActions()){
                    if(current != searching && current.getName().equals(searching.getName())){
                        if(current.equals(searching)){
                            throw new InitializingOfClearRequestObjectException(String.format("Day: %s contains duplicated planned actions",day.getName()));
                        }else {
                            throw new InitializingOfClearRequestObjectException(String.format(
                                    "There are two elements with same names but different bodies ELEMENT 1:{%s}, ELEMENT 2:{%s}",
                                    current,
                                    searching
                            ));
                        }
                    }
                }
            }
        }
    }
    private void manageDuplicatedActionsInDifferentDays(){
        for(int i = 0; i < plannedDays.size(); i++){
            var currentDay = plannedDays.get(i);
            for(int j = 0; j < plannedDays.size(); j++){
                var searchingDay = plannedDays.get(j);
                if(currentDay == searchingDay)
                    continue;
                for(int k = 0; k < plannedDays.get(j).getPlannedActions().size(); k++){
                    var searchingAction = searchingDay.getPlannedActions().get(k);
                    for(int l = 0; l < plannedDays.get(i).getPlannedActions().size(); l++){
                        var currentAction = currentDay.getPlannedActions().get(l);
                        if(currentAction.getName() != null && currentAction.getName().equals(searchingAction.getName())){
                            if(currentAction.equals(searchingAction)){
                                plannedDays.get(j).getPlannedActions().set(k, currentAction);
                            }else {
                                throw new InitializingOfClearRequestObjectException(String.format(
                                        "There are two or more elements with same names but different bodies ELEMENT 1:{%s}, ELEMENT 2: {%s}",
                                        currentAction,
                                        searchingAction
                                ));
                            }
                        }
                    }
                }
            }
        }
    }
    public boolean areThereAnyAblePlannedActionContainers(){
        return plannedActionContainers != null;
    }
    public boolean areThereAnyAblePlannedDayContainers(){
        return plannedDayContainers != null;
    }
}
