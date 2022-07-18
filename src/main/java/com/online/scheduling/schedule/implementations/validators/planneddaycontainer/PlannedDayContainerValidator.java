package com.online.scheduling.schedule.implementations.validators.planneddaycontainer;

import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.validators.generalized.PlannedStuffValidationTemplate;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.repositories.PlannedDayContainerRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannedDayContainerValidator implements IPlannedStuffValidator<PlannedDayContainer> {
    @Getter
    private final PlannedStuffValidationTemplate<PlannedDayContainer> plannedActionValidator;

    public PlannedDayContainerValidator(
            List<IPlannedStuffValidatorComponent<PlannedDayContainer>> actionValidatorComponents,
            PlannedDayContainerRepository actionRepository,
            UserAccountRepository userAccountRepository) {
        this.plannedActionValidator = new PlannedStuffValidationTemplate<>(
                actionValidatorComponents,
                actionRepository,
                actionRepository,
                userAccountRepository,
                "Planned day container");
    }


    @Override
    public boolean validate(PlannedDayContainer validated) {
        return plannedActionValidator.getPlannedStuffValidator().validate(validated);
    }

    @Override
    public boolean validateAll(List<PlannedDayContainer> validated) {
        return plannedActionValidator.getPlannedStuffValidator().validateAll(validated);
    }

    @Override
    public boolean validateUsing(PlannedDayContainer validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateUsing(validated, useToValidate);
    }

    @Override
    public boolean validateAllUsing(List<PlannedDayContainer> validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllUsing(validated, useToValidate);
    }

    @Override
    public boolean validateExcluding(PlannedDayContainer validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateExcluding(validated, exclude);
    }

    @Override
    public boolean validateAllExcluding(List<PlannedDayContainer> validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllExcluding(validated, exclude);
    }
}
