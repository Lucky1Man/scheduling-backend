package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer.components;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

import java.util.ArrayList;
import java.util.List;

public class PSCBasicInitializerComponentImpl<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffContainerRequirements<T>>
        implements IPlannedStuffContainerInitializerComponent<T, R_TO_IN> {

    private final IPlannedStuffValidator<T> stuffValidator;

    public PSCBasicInitializerComponentImpl(
            IPlannedStuffValidator<T> stuffValidator) {
        this.stuffValidator = stuffValidator;
    }

    @Override
    public List<T> initPartOfPlannedStuff(List<T> initialized,List<R_TO_IN> required) {
        if(initialized == null)
            initialized = new ArrayList<>();
        for(R_TO_IN RSTIComponent : required){
            T actionContainer = RSTIComponent.getPlannedStuffContainer();
            if(actionContainer != null && stuffValidator.validate(actionContainer)){
                initialized.add(actionContainer);
            }
        }
        return initialized;
    }
}
