package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff.components;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.ArrayList;
import java.util.List;

public class PSBasicInitializerComponentImpl<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffRequirements<T>>
        implements IPlannedStuffInitializerComponent<T, R_TO_IN> {

    @Override
    public List<T> initPartOfPlannedStuff(List<T> initialized, List<R_TO_IN> required) {
        if (initialized == null)
            initialized = new ArrayList<>();
//        searchForDuplicatedInitializedInOneRequired(required);
        for (R_TO_IN RSTIComponent : required) {
            List<T> toInit = RSTIComponent.getPlannedStuff();
            for (T current : toInit) {
                boolean alreadyAdded = false;
                for (var searching : initialized) {
                    if(searching.getName() == null || current.getName() == null)
                        continue;
                    if (searching.getName().equals(current.getName())) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (!alreadyAdded && current.getId() == null)
                    initialized.add(current);
            }
        }
//        manageDuplicated(required);
        return initialized;
    }
}
