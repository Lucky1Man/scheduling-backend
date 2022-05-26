package com.online.scheduling.schedule.models;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedAction {
    private LocalTime startsAt;
    private LocalTime endsAt;
    private String name;
    private String description;
    private LocalTime remindBefore = null; //TODO: implement reminder feature
}
