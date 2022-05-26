package com.online.scheduling.schedule.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import java.time.temporal.WeekFields;
import java.util.List;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedWeek {
    private Long week;
    @ManyToMany
    private List<PlannedDayContainer> plannedDays;
}
