package com.online.scheduling.schedule.implementations.validators.plannedactioncontainer;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.validators.generalized.PlannedStuffValidationTemplate;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.repositories.PlannedActionContainerRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannedActionContainerValidator implements IPlannedStuffValidator<PlannedActionContainer> {

    @Getter
    private final PlannedStuffValidationTemplate<PlannedActionContainer> plannedActionValidator;

    public PlannedActionContainerValidator(
            List<IPlannedStuffValidatorComponent<PlannedActionContainer>> actionValidatorComponents,
            PlannedActionContainerRepository actionRepository,
            UserAccountRepository userAccountRepository) {
        this.plannedActionValidator = new PlannedStuffValidationTemplate<>(
                actionValidatorComponents,
                actionRepository,
                actionRepository,
                userAccountRepository,
                "Planned action container");
    }

//    public boolean validateAll(PlannedActionContainer validated) throws ValidationExceptionGivenUnexistingId {
//        return plannedActionValidator.getPlannedStuffValidator().validate(validated);
//    }

    @Override
    public boolean validate(PlannedActionContainer validated) {
        return plannedActionValidator.getPlannedStuffValidator().validate(validated);
    }

    @Override
    public boolean validateAll(List<PlannedActionContainer> toValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateAll(toValidate);
    }

    @Override
    public boolean validateUsing(PlannedActionContainer validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateUsing(validated,useToValidate);
    }

    @Override
    public boolean validateAllUsing(List<PlannedActionContainer> validated, List<Long> useToValidate) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllUsing(validated,useToValidate);
    }

    @Override
    public boolean validateExcluding(PlannedActionContainer validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateExcluding(validated,exclude);
    }

    @Override
    public boolean validateAllExcluding(List<PlannedActionContainer> validated, List<Long> exclude) {
        return plannedActionValidator.getPlannedStuffValidator().validateAllExcluding(validated,exclude);
    }
}
