package com.online.scheduling.schedule.implementations.validators.schedule;

import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.implementations.validators.generalized.PlannedStuffValidationTemplate;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.repositories.ScheduleRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleValidator implements IPlannedStuffValidator<Schedule> {
    @Getter
    private final PlannedStuffValidationTemplate<Schedule> scheduleValidator;

    public ScheduleValidator(
            List<IPlannedStuffValidatorComponent<Schedule>> scheduleValidatorComponents,
            ScheduleRepository scheduleRepository,
            UserAccountRepository userAccountRepository) {
        this.scheduleValidator = new PlannedStuffValidationTemplate<>(
                scheduleValidatorComponents,
                scheduleRepository,
                scheduleRepository,
                userAccountRepository,
                "Schedule");
    }

    @Override
    public boolean validate(Schedule validated) {
        return scheduleValidator.getPlannedStuffValidator().validate(validated);
    }

    @Override
    public boolean validateAll(List<Schedule> validated) {
        return scheduleValidator.getPlannedStuffValidator().validateAll(validated);
    }

    @Override
    public boolean validateUsing(Schedule validated, List<Long> useToValidate) {
        return scheduleValidator.getPlannedStuffValidator().validateUsing(validated,useToValidate);
    }

    @Override
    public boolean validateAllUsing(List<Schedule> validated, List<Long> useToValidate) {
        return scheduleValidator.getPlannedStuffValidator().validateAllUsing(validated,useToValidate);
    }

    @Override
    public boolean validateExcluding(Schedule validated, List<Long> exclude) {
        return scheduleValidator.getPlannedStuffValidator().validateExcluding(validated,exclude);
    }

    @Override
    public boolean validateAllExcluding(List<Schedule> validated, List<Long> exclude) {
        return scheduleValidator.getPlannedStuffValidator().validateAllExcluding(validated,exclude);
    }
}
