package com.online.scheduling.schedule.implementations.validators.plannedaction;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.implementations.validators.generalized.PlannedStuffValidationTemplate;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.repositories.PlannedActionRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannedActionValidator implements IPlannedStuffValidator<PlannedAction> {
    @Getter
    private final PlannedStuffValidationTemplate<PlannedAction> plannedActionValidator;

    public PlannedActionValidator(
            List<IPlannedStuffValidatorComponent<PlannedAction>> actionValidatorComponents,
            PlannedActionRepository actionRepository,
            UserAccountRepository userAccountRepository) {
        this.plannedActionValidator = new PlannedStuffValidationTemplate<>(
                actionValidatorComponents,
                actionRepository,
                actionRepository,
                userAccountRepository,
                "Planned action");
    }
    @Override
    public boolean validate(PlannedAction validated) {
        return plannedActionValidator.getPlannedStuffValidator().validate(validated);
    }

    @Override
    public boolean validateAll(List<PlannedAction> validated) {
        return plannedActionValidator.getPlannedStuffValidator().validateAll(validated);
    }

    @Override
    public boolean validateUsing(PlannedAction validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateUsing(validated,useToValidate);
    }

    @Override
    public boolean validateAllUsing(List<PlannedAction> validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllUsing(validated,useToValidate);
    }

    @Override
    public boolean validateExcluding(PlannedAction validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateExcluding(validated,exclude);
    }

    @Override
    public boolean validateAllExcluding(List<PlannedAction> validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllExcluding(validated, exclude);
    }
}
