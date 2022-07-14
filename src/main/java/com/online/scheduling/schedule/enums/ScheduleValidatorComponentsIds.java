package com.online.scheduling.schedule.enums;

import lombok.Getter;

public enum ScheduleValidatorComponentsIds {
    S_OWNER_VALIDATOR_COMPONENT(4L);
    @Getter
    private final long id;

    ScheduleValidatorComponentsIds(long id) {
        this.id = id;
    }
}
