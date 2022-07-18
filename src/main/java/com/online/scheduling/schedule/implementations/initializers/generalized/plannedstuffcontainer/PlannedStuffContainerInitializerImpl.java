package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer;

import com.online.scheduling.exceptions.InitializingOfClearRequestObjectException;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerConfig;

import java.util.List;


public class PlannedStuffContainerInitializerImpl<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffContainerRequirements<T>> {
    private final IPlannedStuffContainerInitializerConfig<T, R_TO_IN> initializerConfig;

    public PlannedStuffContainerInitializerImpl(
            IPlannedStuffContainerInitializerConfig<T, R_TO_IN> initializerConfig) {
        this.initializerConfig = initializerConfig;

    }

    public List<T> doInit(List<T> initialized, List<R_TO_IN> requiredToInit) throws RuntimeException{
        for(var component : initializerConfig.getContainerInitializerComponents()){
            initialized = component.initPartOfPlannedStuff(initialized, requiredToInit);
        }

        deleteDuplicates(initialized);
        manageDuplicatesInContainerOwners(requiredToInit);
        return initialized;
    }

    private void deleteDuplicates(List<T> initialized){
        for(int i = 0; i < initialized.size(); i++){
            var current = initialized.get(i);
            for(int j = 0; j < initialized.size(); j++){
                var searching = initialized.get(j);
                String currentName = current.getName();
                if(currentName != null && currentName.equals(searching.getName()) && current != searching){
                    if(current.equals(searching)){
                        initialized.remove(j);
                        j--;
                    }else {
                        throw new InitializingOfClearRequestObjectException(String.format("Elements has same name but different bodies first: %s, second: %s ",
                                current, searching
                        ));
                    }
                }
            }
        }
    }

    private void manageDuplicatesInContainerOwners(List<R_TO_IN> containerOwner){
        for(var current : containerOwner){
            for(var searching : containerOwner){
                if(current != searching
                        && current.getPlannedStuffContainer() != null
                        && searching.getPlannedStuffContainer() != null
                        && current.getPlannedStuffContainer().equals(searching.getPlannedStuffContainer())){
                    searching.setPlannedStuffContainer(current.getPlannedStuffContainer());
                }
            }
        }
    }
}
