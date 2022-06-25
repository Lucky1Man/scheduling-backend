package com.online.scheduling.schedule.config.excluded;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequired;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerConfig;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorConfig;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PlannedActionContainerConfigImpl implements
        IPlannedStuffValidatorConfig<PlannedActionContainer>
        {

    private final List<IPlannedStuffValidatorComponent<PlannedActionContainer>> validatorComponents;

    public PlannedActionContainerConfigImpl(
            List<IPlannedStuffValidatorComponent<PlannedActionContainer>> validatorComponents) {
        this.validatorComponents = validatorComponents;
    }

    @Override
    public List<IPlannedStuffValidatorComponent<PlannedActionContainer>> getValidatorComponents() {
        return validatorComponents;
    }
}
