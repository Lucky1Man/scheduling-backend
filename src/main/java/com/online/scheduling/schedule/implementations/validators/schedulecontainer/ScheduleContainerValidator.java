package com.online.scheduling.schedule.implementations.validators.schedulecontainer;

import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.validators.generalized.PlannedStuffValidationTemplate;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.repositories.ScheduleContainerRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleContainerValidator implements IPlannedStuffValidator<ScheduleContainer> {
    @Getter
    private final PlannedStuffValidationTemplate<ScheduleContainer> scheduleContainerValidator;

    public ScheduleContainerValidator(
            List<IPlannedStuffValidatorComponent<ScheduleContainer>> scheduleValidatorComponents,
            ScheduleContainerRepository scheduleContainerRepository,
            UserAccountRepository userAccountRepository) {
        this.scheduleContainerValidator = new PlannedStuffValidationTemplate<>(
                scheduleValidatorComponents,
                scheduleContainerRepository,
                scheduleContainerRepository,
                userAccountRepository,
                "Schedule container");
    }

    @Override
    public boolean validate(ScheduleContainer validated) {
        return scheduleContainerValidator.getPlannedStuffValidator().validate(validated);
    }

    @Override
    public boolean validateAll(List<ScheduleContainer> validated) {
        return scheduleContainerValidator.getPlannedStuffValidator().validateAll(validated);
    }

    @Override
    public boolean validateUsing(ScheduleContainer validated, List<Long> useToValidate) {
        return scheduleContainerValidator.getPlannedStuffValidator().validateUsing(validated, useToValidate);
    }

    @Override
    public boolean validateAllUsing(List<ScheduleContainer> validated, List<Long> useToValidate) {
        return scheduleContainerValidator.getPlannedStuffValidator().validateAllUsing(validated, useToValidate);
    }

    @Override
    public boolean validateExcluding(ScheduleContainer validated, List<Long> exclude) {
        return scheduleContainerValidator.getPlannedStuffValidator().validateExcluding(validated, exclude);
    }

    @Override
    public boolean validateAllExcluding(List<ScheduleContainer> validated, List<Long> exclude) {
        return scheduleContainerValidator.getPlannedStuffValidator().validateAllExcluding(validated, exclude);
    }
}
