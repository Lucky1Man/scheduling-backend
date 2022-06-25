package com.online.scheduling.schedule.entities.excluded;

import lombok.*;

import javax.persistence.*;

@Entity(name = "plans")
@NoArgsConstructor
@AllArgsConstructor()
@ToString
@Getter
@Setter
@Builder
public class PlansContainer {
    @Id
    @SequenceGenerator(
            name = "plans_sequence",
            sequenceName = "plans_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "plans_sequence"
    )
    private Long id;
    @Embedded
    private Plans plans;
}
