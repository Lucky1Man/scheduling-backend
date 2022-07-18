package com.online.scheduling.schedule.implementations.validators.generalized.components;

import com.online.scheduling.exceptions.ValidationExceptionGivenBadName;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class PSIfNameTakenValidatorComponentImpl<T extends IPlannedStuff<T>> {

    private final IPlannedStuffRepository<T> plannedStuffRepository;

    @Setter
    @Getter
    private String prefix;

    public PSIfNameTakenValidatorComponentImpl(
            IPlannedStuffRepository<T> plannedStuffRepository) {
        this.plannedStuffRepository = plannedStuffRepository;
    }


    public boolean isValid(IPlannedStuff<T> validated){
        String name = validated.getName();
        Optional<T> byName = plannedStuffRepository.findByName(name);
        if(byName.isPresent()
                && validated.getUserAccount().equals(byName.get().getUserAccount())
                && !byName.get().getId().equals(validated.getId()))
            throw new ValidationExceptionGivenBadName(String.format("%s with name %s  already exists", prefix, name));
        return true;
    }
}
