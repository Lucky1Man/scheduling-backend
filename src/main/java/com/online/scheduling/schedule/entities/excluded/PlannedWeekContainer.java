package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.*;

@Entity(name = "planned_week")
@NoArgsConstructor
@AllArgsConstructor()
@ToString
@Getter
@Setter
@Builder
public class PlannedWeekContainer {
    @Id
    @SequenceGenerator(
            name = "planned_week_sequence",
            sequenceName = "planned_week_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_week_sequence"
    )
    private Long id;
    @Embedded
    private PlannedWeek plannedWeek;
}
