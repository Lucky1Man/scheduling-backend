package com.online.scheduling.schedule.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "planned_action")
@NoArgsConstructor
@AllArgsConstructor()
@Data
@Builder
public class PlannedActionContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private PlannedAction plannedAction;
}
