package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer;

import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer.components.PSCBasicInitializerComponentImpl;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerConfig;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PlannedStuffContainerInitializerTemplate<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffContainerRequirements<T>> {
    @Setter
    private IPlannedStuffContainerInitializerConfig<T, R_TO_IN> stuffInitializerConfig;
    @Setter
    @Getter
    private PlannedStuffContainerInitializerImpl<T, R_TO_IN> stuffContainerInitializer;
    private final PlannedStuffContainerBasicInitializerComponentWrapper initializerComponentWrapper1;

    public PlannedStuffContainerInitializerTemplate(
            List<IPlannedStuffContainerInitializerComponent<T, R_TO_IN>> stuffContainerInitializerComponents,
            IPlannedStuffValidator<T> stuffValidator){
        this.initializerComponentWrapper1 = new PlannedStuffContainerBasicInitializerComponentWrapper(stuffValidator);
        stuffContainerInitializerComponents.add(0, initializerComponentWrapper1);
        this.stuffInitializerConfig = new PSCInitializerConfigImpl<>(stuffContainerInitializerComponents);
        this.stuffContainerInitializer = new PlannedStuffContainerInitializerImpl<>(stuffInitializerConfig);
    }

    private class PlannedStuffContainerBasicInitializerComponentWrapper implements
            IPlannedStuffContainerInitializerComponent<T, R_TO_IN>{

        private final PSCBasicInitializerComponentImpl<T, R_TO_IN> defaultContainerInitializer;

        private PlannedStuffContainerBasicInitializerComponentWrapper(
                IPlannedStuffValidator<T> stuffValidator) {
            this.defaultContainerInitializer = new PSCBasicInitializerComponentImpl<>(stuffValidator);
        }

        @Override
        public List<T> initPartOfPlannedStuff(List<T> initialized, List<R_TO_IN> required) {
            return defaultContainerInitializer.initPartOfPlannedStuff(initialized,required);
        }
    }
}
