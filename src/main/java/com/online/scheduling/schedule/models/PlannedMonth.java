package com.online.scheduling.schedule.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import java.time.Month;
import java.util.List;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedMonth {
    private Month month;
    @ManyToMany
    private List<PlannedWeekContainer> plannedWeeks;
}
