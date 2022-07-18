package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff;

import com.online.scheduling.exceptions.InitializingOfClearRequestObjectException;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerConfig;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.List;

public class PlannedStuffInitializerImpl <T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffRequirements<T>>{
    private final IPlannedStuffInitializerConfig<T, R_TO_IN> initializerConfig;

    public PlannedStuffInitializerImpl(
            IPlannedStuffInitializerConfig<T, R_TO_IN> initializerConfig) {
        this.initializerConfig = initializerConfig;

    }

    public List<T> doInit(List<T> initialized, List<R_TO_IN> requiredToInit){
        searchForDuplicatedInitializedInOneRequired(requiredToInit);
        for(var component : initializerConfig.getInitializerComponents()){
            initialized = component.initPartOfPlannedStuff(initialized, requiredToInit);
        }
        manageDuplicated(requiredToInit);
        return initialized;
    }

    private void searchForDuplicatedInitializedInOneRequired(List<R_TO_IN> required) {
        for (var reqComp : required) {
            for (var curInitComp : reqComp.getPlannedStuff()) {
                for (var searInitComp : reqComp.getPlannedStuff()) {
                    if(curInitComp.getName() == null || searInitComp.getName() == null)
                        continue;
                    if (curInitComp != searInitComp && curInitComp.getName().equals(searInitComp.getName())) {
                        if (curInitComp.equals(searInitComp)) {
                            throw new InitializingOfClearRequestObjectException(String.format(
                                    "Object {%s} contains duplicated objects {%s}", reqComp, curInitComp));
                        } else {
                            throw new InitializingOfClearRequestObjectException(String.format(
                                    "There are two elements with same names but different bodies {%s} {%s}",
                                    reqComp,
                                    searInitComp
                            ));
                        }
                    }
                }
            }
        }
    }

    private void manageDuplicated(List<R_TO_IN> required) {
        for (int i = 0; i < required.size(); i++) {
            var currentDay = required.get(i);
            for (int j = 0; j < required.size(); j++) {
                var searchingDay = required.get(j);
                if (currentDay == searchingDay)
                    continue;
                for (int k = 0; k < required.get(j).getPlannedStuff().size(); k++) {
                    var searchingAction = searchingDay.getPlannedStuff().get(k);
                    for (int l = 0; l < required.get(i).getPlannedStuff().size(); l++) {
                        var currentAction = currentDay.getPlannedStuff().get(l);
                        if (currentAction.getName() != null && currentAction.getName().equals(searchingAction.getName())) {
                            if (currentAction.equals(searchingAction)) {
                                required.get(j).getPlannedStuff().set(k, currentAction);
                            } else {
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

}
