package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import java.time.Year;
import java.util.List;

@Embeddable
@ToString(
        exclude = "plannedMonths"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedYear {
    @Column(
            columnDefinition = "TEXT(50)"
    )
    private Year year; //TODO search for info how to save years in sql
    private String name;
    @ManyToMany
    private List<PlannedMonthContainer> plannedMonths;
}
