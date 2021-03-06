package com.online.scheduling.schedule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity(name = "planned_day")
@ToString(
        exclude = {"plannedActions"}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "planned_day_name", columnNames = {"name","user_account_id"})
        }
)
public class PlannedDay implements
        IPlannedStuff<PlannedDay>,
        IPlannedStuffContainerRequirements<PlannedDayContainer>,
        IPlannedStuffRequirements<PlannedAction> {
    @Id
    @SequenceGenerator(
            name = "planned_day_sequence",
            sequenceName = "planned_day_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_day_sequence"
    )
    private Long id;
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "planned_day_planned_actions",
            joinColumns = {
                    @JoinColumn(
                            foreignKey = @ForeignKey(name = "day_id"),
                            name = "day_id",
                            table = "planned_day",
                            referencedColumnName = "id"
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            foreignKey = @ForeignKey(name = "action_id"),
                            name = "action_id",
                            table = "planned_action",
                            referencedColumnName = "id"
                    )
            },
            indexes = {
                    @Index(name = "day_id", columnList = "day_id"),
                    @Index(name = "action_id", columnList = "action_id")
            }
    )
    private List<PlannedAction> plannedActions;

    @ManyToOne
    @JoinColumn(
            referencedColumnName = "id",
            name = "planned_day_container_id"
    )
    private PlannedDayContainer plannedDayContainer;
    @ManyToOne
    @JoinColumn(
            name = "user_account_id",
            referencedColumnName = "id"
    )
    private UserAccount userAccount;



    @Override
    @JsonIgnore
    public PlannedDayContainer getPlannedStuffContainer() {
        return getPlannedDayContainer();
    }

    @Override
    public void setPlannedStuffContainer(PlannedDayContainer container) {
        this.plannedDayContainer = container;
    }

    @Override
    @JsonIgnore
    public List<PlannedAction> getPlannedStuff() {
        return getPlannedActions();
    }

    public PlannedDay(PlannedDay day) {
        this.id = day.id;
        this.date = day.date;
        this.dayOfWeek = day.dayOfWeek;
        this.name = day.name;
        this.plannedActions = day.plannedActions;
        this.plannedDayContainer = day.plannedDayContainer;
        this.userAccount = day.userAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlannedDay that = (PlannedDay) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && dayOfWeek == that.dayOfWeek && Objects.equals(name, that.name) && Objects.equals(plannedActions, that.plannedActions) && Objects.equals(plannedDayContainer, that.plannedDayContainer);
    }

    @Override
    public PlannedDay copyProperties(PlannedDay from) {
        this.id = from.id;
        this.name = from.name;
        this.date = from.date;
        this.dayOfWeek = from.dayOfWeek;
        this.plannedActions = from.plannedActions;
        this.plannedDayContainer = from.plannedDayContainer;
        this.userAccount = from.userAccount;
        return this;
    }

    @Override
    public boolean isBlank() {
        return IPlannedStuff.super.isBlank();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, dayOfWeek, name, plannedActions, plannedDayContainer);
    }


}
