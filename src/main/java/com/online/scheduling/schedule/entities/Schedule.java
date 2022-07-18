package com.online.scheduling.schedule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;
import com.online.scheduling.user.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    public Schedule(Schedule schedule) {
        this.id = schedule.id;
        this.name = schedule.name;
        this.owner = schedule.owner;
        this.members = schedule.members;
        this.days = schedule.days;
        this.scheduleContainer = schedule.scheduleContainer;
        this.userAccount = schedule.userAccount;
    }

    @Override
    public Schedule copyProperties(Schedule from) {
        this.id = from.id;
        this.name = from.name;
        this.owner = from.owner;
        this.members = from.members;
        this.days = from.days;
        this.scheduleContainer = from.scheduleContainer;
        this.userAccount = from.userAccount;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) && Objects.equals(name, schedule.name) && Objects.equals(owner, schedule.owner) && Objects.equals(members, schedule.members) && Objects.equals(days, schedule.days) && Objects.equals(scheduleContainer, schedule.scheduleContainer) && Objects.equals(userAccount, schedule.userAccount);
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, members, days, scheduleContainer, userAccount);
    }
}
