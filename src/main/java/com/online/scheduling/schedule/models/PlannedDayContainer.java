package com.online.scheduling.schedule.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "planned_day")
@NoArgsConstructor
@AllArgsConstructor()
@Data
@Builder
public class PlannedDayContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private PlannedDay plannedDay;
}
