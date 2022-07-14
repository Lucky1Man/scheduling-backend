package com.online.scheduling.schedule.implementations.validators.generalized;

import com.online.scheduling.exceptions.DuplicatesFoundException;
import com.online.scheduling.exceptions.PlannedStuffConflictException;
import com.online.scheduling.exceptions.ValidationExceptionGivenNonExistingId;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorConfig;

import java.util.List;

public class PlannedStuffValidatorImpl<T extends IPlannedStuff<T>> implements IPlannedStuffValidator<T> {
    private final IPlannedStuffValidatorConfig<T> validatorConfig;

    public PlannedStuffValidatorImpl(
            IPlannedStuffValidatorConfig<T> validatorConfig) {
        this.validatorConfig = validatorConfig;
    }

    @Override
    public boolean validate(T validated) throws ValidationExceptionGivenNonExistingId {
        for(var validatorComponent: validatorConfig.getValidatorComponents()){
            if(!validatorComponent.isValid(validated)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validateAll(List<T> toValidate) {
        searchForDuplicated(toValidate);
        for(var validated : toValidate){
            if(!validate(validated)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validateUsing(T validated, List<Long> useToValidate) {
        for(int i = 0; i < validatorConfig.getValidatorComponents().size(); i++){
            for (Long aLong : useToValidate) {
                if (i == aLong) {
                    if (!validatorConfig.getValidatorComponents().get(i).isValid(validated)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean validateAllUsing(List<T> validated, List<Long> useToValidate) {
        searchForDuplicated(validated);
        for(var val : validated){
            if(!validateUsing(val, useToValidate))
                return false;
        }
        return true;
    }

    @Override
    public boolean validateExcluding(T validated, List<Long> exclude) {
        for(int i = 0; i < validatorConfig.getValidatorComponents().size(); i++){
            for (Long aLong : exclude) {
                if (i != aLong) {
                    if (!validatorConfig.getValidatorComponents().get(i).isValid(validated)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean validateAllExcluding(List<T> validated, List<Long> exclude) {
        searchForDuplicated(validated);
        for(var val : validated){
            if(!validateExcluding(val, exclude))
                return false;
        }
        return true;
    }

    private void searchForDuplicated(List<T> validated){
        for(var val : validated){
            for(var val2 : validated){
                if(val != val2 && val.getName()!=null && val2.getName() != null && val.getName().equals(val2.getName())){
                    if(val.equals(val2)){
                        throw new DuplicatesFoundException(String.format(
                                "There are duplicates of this object: %s", val));
                    }else {
                        throw new PlannedStuffConflictException(String.format(
                                "There are two objects with same name but different bodies elem1: {%s}, elem2: {%s}",
                                val, val2));
                    }
                }
            }
        }
    }

}
