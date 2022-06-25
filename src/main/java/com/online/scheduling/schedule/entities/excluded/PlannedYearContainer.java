package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.*;

@Entity(name = "planned_year")
@NoArgsConstructor
@AllArgsConstructor()
@ToString
@Getter
@Setter
@Builder
public class PlannedYearContainer {
    @Id
    @SequenceGenerator(
            name = "planned_year_sequence",
            sequenceName = "planned_year_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_year_sequence"
    )
    private Long id;
    @Embedded
    private PlannedYear plannedYear;
}
