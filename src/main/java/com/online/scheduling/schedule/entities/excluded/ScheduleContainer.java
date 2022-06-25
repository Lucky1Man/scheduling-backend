package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.*;

@Entity(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor()
@ToString
@Getter
@Setter
@Builder
public class ScheduleContainer {
    @Id
    @SequenceGenerator(
            name = "schedule_sequence",
            sequenceName = "schedule_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "schedule_sequence"
    )
    private Long id;
    @Embedded
    private Schedule schedule;
}
