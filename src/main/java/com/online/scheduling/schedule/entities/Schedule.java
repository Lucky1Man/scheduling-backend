package com.online.scheduling.schedule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.online.scheduling.schedule.implementations.json.serealize.UserAccountSerializer;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;
import com.online.scheduling.user.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "schedule")
@ToString(
        exclude = {"members"}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "schedule_name", columnNames = {"name","user_account_id"})
        }
)
public class Schedule implements
        IPlannedStuff<Schedule>,
        IPlannedStuffContainerRequirements<ScheduleContainer>,
        IPlannedStuffRequirements<PlannedDay> {
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
    @Column(
            nullable = false
    )
    private String name;
    @ManyToOne
    private User owner;
    @ManyToMany
    private List<User> members;
    @ManyToMany
    private List<PlannedDay> days;
    @ManyToOne
    @JoinColumn(
            referencedColumnName = "id",
            name = "schedule_container_id"
    )
    private ScheduleContainer scheduleContainer;
    @ManyToOne
    @JoinColumn(
            name = "user_account_id",
            referencedColumnName = "id"
    )
    private UserAccount userAccount;
    @Override
    @JsonIgnore
    public ScheduleContainer getPlannedStuffContainer() {
        return getScheduleContainer();
    }

    @Override
    public void setPlannedStuffContainer(ScheduleContainer container) {
        setScheduleContainer(container);
    }

    @Override
    @JsonIgnore
    public List<PlannedDay> getPlannedStuff() {
        return getDays();
    }
}
