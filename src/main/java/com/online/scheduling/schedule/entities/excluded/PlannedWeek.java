package com.online.scheduling.schedule.entities.excluded;

import com.online.scheduling.schedule.entities.PlannedDayContainer;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import java.util.List;

@Embeddable
@ToString(
        exclude = "plannedDays"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedWeek {
    private Long week;
    private String name;
    @ManyToMany
    private List<PlannedDayContainer> plannedDays;
}
