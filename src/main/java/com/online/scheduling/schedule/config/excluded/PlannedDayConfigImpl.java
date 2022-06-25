package com.online.scheduling.schedule.config.excluded;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorConfig;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PlannedDayConfigImpl implements IPlannedStuffValidatorConfig<PlannedDay> {
    private final List<IPlannedStuffValidatorComponent<PlannedDay>> validatorComponents;

    public PlannedDayConfigImpl(
            List<IPlannedStuffValidatorComponent<PlannedDay>> validatorComponents) {
        this.validatorComponents = validatorComponents;
    }

    @Override
    public List<IPlannedStuffValidatorComponent<PlannedDay>> getValidatorComponents() {
        return validatorComponents;
    }
}
