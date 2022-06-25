package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import java.time.Month;
import java.util.List;

@Embeddable
@ToString(
        exclude = "plannedWeeks"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedMonth {
    private Month month;
    private String name;
    @ManyToMany
    private List<PlannedWeekContainer> plannedWeeks;
}
