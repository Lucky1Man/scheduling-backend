package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
@ToString(
        exclude = "plannedYears"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plans {
    private String name;
    @OneToMany
    private List<PlannedYearContainer> plannedYears;
}
