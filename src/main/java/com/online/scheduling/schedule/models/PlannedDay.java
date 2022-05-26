package com.online.scheduling.schedule.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedDay {
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    @ManyToMany
    private List<PlannedActionContainer> plannedActions;
}
