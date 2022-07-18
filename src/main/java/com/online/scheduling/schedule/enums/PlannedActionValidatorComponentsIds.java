package com.online.scheduling.schedule.enums;

import lombok.Getter;

public enum PlannedActionValidatorComponentsIds {
    PA_TIME_BORDERS_VALIDATOR_COMPONENT(4L);

    @Getter
    private final long id;

    PlannedActionValidatorComponentsIds(long id) {
        this.id = id;
    }

}
