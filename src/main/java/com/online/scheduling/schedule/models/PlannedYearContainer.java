package com.online.scheduling.schedule.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "planned_year")
@NoArgsConstructor
@AllArgsConstructor()
@Data
@Builder
public class PlannedYearContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private PlannedYear plannedYear;
}
