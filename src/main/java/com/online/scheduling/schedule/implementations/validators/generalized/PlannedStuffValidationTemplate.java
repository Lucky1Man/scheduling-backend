package com.online.scheduling.schedule.implementations.validators.generalized;

import com.online.scheduling.exceptions.ValidationExceptionGivenNonExistingId;
import com.online.scheduling.schedule.implementations.validators.generalized.components.PSIdValidatorComponentImpl;
import com.online.scheduling.schedule.implementations.validators.generalized.components.PSIfNameTakenValidatorComponentImpl;
import com.online.scheduling.schedule.implementations.validators.generalized.components.PSNameValidatorComponentImpl;
import com.online.scheduling.schedule.implementations.validators.generalized.components.PSUserAccountValidatorComponentImpl;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorConfig;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public class PlannedStuffValidationTemplate<T extends IPlannedStuff<T>> {
    @Setter
    @Getter
    private IPlannedStuffValidator<T> plannedStuffValidator;
    @Setter
    @Getter
    private IPlannedStuffValidatorConfig<T> plannedStuffValidatorConfig;
    private final PlannedStuffNameValidatorComponentWrapper component1;
    private final PlannedStuffIdValidatorComponentWrapper component2;
    private final PlannedStuffIfAlreadyUsedNameValidationComponentWrapper component3;
    private final PlannedStuffUserAccountValidatorComponentWrapper component4;
    private String prefix = "Planned stuff";

    public PlannedStuffValidationTemplate(
            List<IPlannedStuffValidatorComponent<T>> validatorComponents,
            JpaRepository<T, Long> plannedStuffRepository,
            IPlannedStuffRepository<T> plannedStuffRepository2,
            UserAccountRepository userAccountRepository) {
        component2 = new PlannedStuffIdValidatorComponentWrapper(plannedStuffRepository);
        validatorComponents.add(0, component2);
        component1 = new PlannedStuffNameValidatorComponentWrapper();
        validatorComponents.add(1, component1);
        component3 = new PlannedStuffIfAlreadyUsedNameValidationComponentWrapper(plannedStuffRepository2);
        validatorComponents.add(2,component3);
        component4 = new PlannedStuffUserAccountValidatorComponentWrapper(userAccountRepository);
        validatorComponents.add(3, component4);

        this.plannedStuffValidatorConfig = new PlannedStuffValidatorConfigImpl<>(validatorComponents);
        this.plannedStuffValidator = new PlannedStuffValidatorImpl<T>(plannedStuffValidatorConfig);
    }

    public PlannedStuffValidationTemplate(
            List<IPlannedStuffValidatorComponent<T>> validatorComponents,
            JpaRepository<T, Long> plannedStuffRepository,
            IPlannedStuffRepository<T> plannedStuffRepository2,
            UserAccountRepository userAccountRepository,
            String prefix) {
        this(validatorComponents, plannedStuffRepository, plannedStuffRepository2, userAccountRepository);
        this.prefix = prefix;
        resetPrefix(this.prefix);
    }
    private void resetPrefix(String prefix){
        component1.setNameValidatorPrefix(prefix);
        component2.setIdValidatorPrefix(prefix);
        component3.setNameValidatorPrefix(prefix);
        component4.setPrefix(prefix);
    }
    private class PlannedStuffIdValidatorComponentWrapper implements IPlannedStuffValidatorComponent<T>{
        private final PSIdValidatorComponentImpl<T> defaultIdValidatorComponent;

        private PlannedStuffIdValidatorComponentWrapper(
                JpaRepository<T, Long> stuffRepository) {
            this.defaultIdValidatorComponent = new PSIdValidatorComponentImpl<>(stuffRepository);
            defaultIdValidatorComponent.setPrefix(prefix);
        }

        @Override
        public boolean isValid(T validated) throws ValidationExceptionGivenNonExistingId {
            return defaultIdValidatorComponent.isValid(validated);
        }

        private void setIdValidatorPrefix(String prefix){
            defaultIdValidatorComponent.setPrefix(prefix);
        }
    }

    private class PlannedStuffNameValidatorComponentWrapper implements IPlannedStuffValidatorComponent<T>{
        private final PSNameValidatorComponentImpl<T> defaultNameValidatorComponent;

        private PlannedStuffNameValidatorComponentWrapper() {
            this.defaultNameValidatorComponent = new PSNameValidatorComponentImpl<>();
            defaultNameValidatorComponent.setPrefix(prefix);
        }

        @Override
        public boolean isValid(T validated) throws RuntimeException{
            return defaultNameValidatorComponent.isValid(validated);
        }
        private void setNameValidatorPrefix(String prefix){
            defaultNameValidatorComponent.setPrefix(prefix);
        }
    }

    private class PlannedStuffIfAlreadyUsedNameValidationComponentWrapper implements IPlannedStuffValidatorComponent<T>{

        private final PSIfNameTakenValidatorComponentImpl<T> nameValidatorComponent;
        public PlannedStuffIfAlreadyUsedNameValidationComponentWrapper(IPlannedStuffRepository<T> plannedStuffRepository) {
            this.nameValidatorComponent = new PSIfNameTakenValidatorComponentImpl<>(plannedStuffRepository);
            nameValidatorComponent.setPrefix(prefix);
        }
        @Override
        public boolean isValid(T validated) {
            return nameValidatorComponent.isValid(validated);
        }
        private void setNameValidatorPrefix(String prefix){
            nameValidatorComponent.setPrefix(prefix);
        }
    }
    private class PlannedStuffUserAccountValidatorComponentWrapper implements IPlannedStuffValidatorComponent<T>{

        private final PSUserAccountValidatorComponentImpl<T> userAccountValidation;

        private PlannedStuffUserAccountValidatorComponentWrapper(UserAccountRepository userAccountRepository) {
            this.userAccountValidation = new PSUserAccountValidatorComponentImpl<>(userAccountRepository);
            userAccountValidation.setPrefix(prefix);
        }

        @Override
        public boolean isValid(T validated) {
            return userAccountValidation.isValid(validated);
        }
        private void setPrefix(String prefix){
            userAccountValidation.setPrefix(prefix);
        }
    }

}
