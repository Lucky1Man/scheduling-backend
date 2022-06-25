package com.online.scheduling.schedule.implementations.validators.generalized.components;

import com.online.scheduling.exceptions.ValidationExceptionGivenNonExistingId;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

public class PSIdValidatorComponentImpl<T>{

    private final JpaRepository<T, Long> plannedStuffRepository;
    @Setter
    @Getter
    private String prefix;

    public PSIdValidatorComponentImpl(
            JpaRepository<T, Long> plannedStuffRepository) {
        this.plannedStuffRepository = plannedStuffRepository;
    }

    public boolean isValid(IPlannedStuff<T> validated) {
        Long id = validated.getId();
        if(id != null) {
            if(id < 0)
                return false;
            plannedStuffRepository.findById(id).orElseThrow(
                    () -> new ValidationExceptionGivenNonExistingId(String.format("%s with given id %s does not exist", prefix, id))
            );
            return false;
        }
        return true;
    }
}
