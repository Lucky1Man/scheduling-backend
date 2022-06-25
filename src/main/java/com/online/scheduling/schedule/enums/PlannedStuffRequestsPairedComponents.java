package com.online.scheduling.schedule.enums;

import lombok.Getter;

import java.util.List;

import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.*;

public enum PlannedStuffRequestsPairedComponents {
    NAME_VALIDATION_VALIDATOR_CONFIG(
                    List.of(PS_IF_NAME_TAKEN_VALIDATOR_COMPONENT.getId(),
                            PS_NAME_VALIDATOR_COMPONENT.getId())
    );

    PlannedStuffRequestsPairedComponents(List<Long> componentsIds) {
        this.componentsIds = componentsIds;
    }

    @Getter
    private final List<Long> componentsIds;
}
