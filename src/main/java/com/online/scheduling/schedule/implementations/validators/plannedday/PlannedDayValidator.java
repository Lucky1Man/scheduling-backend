package com.online.scheduling.schedule.implementations.validators.plannedday;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.implementations.validators.generalized.PlannedStuffValidationTemplate;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.repositories.PlannedDayRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannedDayValidator implements IPlannedStuffValidator<PlannedDay> {
    @Getter
    private final PlannedStuffValidationTemplate<PlannedDay> plannedActionValidator;

    public PlannedDayValidator(
            List<IPlannedStuffValidatorComponent<PlannedDay>> actionValidatorComponents,
            PlannedDayRepository actionRepository,
            UserAccountRepository userAccountRepository) {
        this.plannedActionValidator = new PlannedStuffValidationTemplate<>(
                actionValidatorComponents,
                actionRepository,
                actionRepository,
                userAccountRepository,
                "Planned day");
    }


    @Override
    public boolean validate(PlannedDay validated) {
        return plannedActionValidator.getPlannedStuffValidator().validate(validated);
    }

    @Override
    public boolean validateAll(List<PlannedDay> validated) {
        return plannedActionValidator.getPlannedStuffValidator().validateAll(validated);
    }

    @Override
    public boolean validateUsing(PlannedDay validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateUsing(validated,useToValidate);
    }

    @Override
    public boolean validateAllUsing(List<PlannedDay> validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllUsing(validated,useToValidate);
    }

    @Override
    public boolean validateExcluding(PlannedDay validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateExcluding(validated, exclude);
    }

    @Override
    public boolean validateAllExcluding(List<PlannedDay> validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllExcluding(validated, exclude);
    }
}
