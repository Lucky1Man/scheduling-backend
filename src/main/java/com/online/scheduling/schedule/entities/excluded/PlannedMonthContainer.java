package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.*;

@Entity(name = "planned_month")
@NoArgsConstructor
@AllArgsConstructor()
@ToString
@Getter
@Setter
@Builder
public class PlannedMonthContainer {
    @Id
    @SequenceGenerator(
            name = "planned_month_sequence",
            sequenceName = "planned_month_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_month_sequence"
    )
    private Long id;
    @Embedded
    private PlannedMonth plannedMonth;
}
