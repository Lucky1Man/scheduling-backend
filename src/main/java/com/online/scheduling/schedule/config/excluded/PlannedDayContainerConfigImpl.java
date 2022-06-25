package com.online.scheduling.schedule.config.excluded;

import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorConfig;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PlannedDayContainerConfigImpl implements IPlannedStuffValidatorConfig<PlannedDayContainer> {
    private final List<IPlannedStuffValidatorComponent<PlannedDayContainer>> validatorComponents;

    public PlannedDayContainerConfigImpl(
            List<IPlannedStuffValidatorComponent<PlannedDayContainer>> validatorComponents) {
        this.validatorComponents = validatorComponents;
    }

    @Override
    public List<IPlannedStuffValidatorComponent<PlannedDayContainer>> getValidatorComponents() {
        return validatorComponents;
    }
}
