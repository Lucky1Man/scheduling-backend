package com.online.scheduling.schedule.enums;

import lombok.Getter;

public enum PlannedStuffValidatorComponentsIds {
    PS_ID_VALIDATOR_COMPONENT(0L),
    PS_NAME_VALIDATOR_COMPONENT(1L),
    PS_IF_NAME_TAKEN_VALIDATOR_COMPONENT(2L),
    PS_USER_ACCOUNT_VALIDATOR_COMPONENT(3L);

    @Getter
    private final Long id;

    PlannedStuffValidatorComponentsIds(long id) {
        this.id = id;
    }
}
