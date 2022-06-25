package com.online.scheduling.schedule.config.excluded;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorConfig;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PlannedActionConfigImpl implements IPlannedStuffValidatorConfig<PlannedAction> {
    private final List<IPlannedStuffValidatorComponent<PlannedAction>> validatorComponents;

    public PlannedActionConfigImpl(
            List<IPlannedStuffValidatorComponent<PlannedAction>> validatorComponents) {
        this.validatorComponents = validatorComponents;
    }

    @Override
    public List<IPlannedStuffValidatorComponent<PlannedAction>> getValidatorComponents() {
        return validatorComponents;
    }
}
