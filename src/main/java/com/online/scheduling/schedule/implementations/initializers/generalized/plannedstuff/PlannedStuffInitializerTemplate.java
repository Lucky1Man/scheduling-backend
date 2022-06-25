package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff;

import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff.components.PSBasicInitializerComponentImpl;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerConfig;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PlannedStuffInitializerTemplate <T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffRequirements<T>>{

    @Setter
    private IPlannedStuffInitializerConfig<T, R_TO_IN> stuffInitializerConfig;
    @Setter
    @Getter
    private PlannedStuffInitializerImpl<T, R_TO_IN> stuffContainerInitializer;

    private final PlannedStuffBasicInitializerComponentWrapper initializerComponentWrapper1;

    public PlannedStuffInitializerTemplate(
            List<IPlannedStuffInitializerComponent<T, R_TO_IN>> stuffContainerInitializerComponents,
            IPlannedStuffValidator<T> stuffValidator) {
        this.initializerComponentWrapper1 = new PlannedStuffBasicInitializerComponentWrapper(stuffValidator);
        stuffContainerInitializerComponents.add(0, initializerComponentWrapper1);
        this.stuffInitializerConfig = new PSInitializerConfigImpl<>(stuffContainerInitializerComponents);
        this.stuffContainerInitializer = new PlannedStuffInitializerImpl<>(stuffInitializerConfig);
    }

    private class PlannedStuffBasicInitializerComponentWrapper implements
            IPlannedStuffInitializerComponent<T, R_TO_IN> {

        private final PSBasicInitializerComponentImpl<T, R_TO_IN> defaultContainerInitializer;

        private PlannedStuffBasicInitializerComponentWrapper(
                IPlannedStuffValidator<T> stuffValidator) {
            this.defaultContainerInitializer = new PSBasicInitializerComponentImpl<>(stuffValidator);
        }
        @Override
        public List<T> initPartOfPlannedStuff(List<T> initialized, List<R_TO_IN> required) {
            return defaultContainerInitializer.initPartOfPlannedStuff(initialized,required);
        }
    }
}
