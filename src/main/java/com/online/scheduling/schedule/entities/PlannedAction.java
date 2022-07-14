package com.online.scheduling.schedule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity(name = "planned_action")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "name_account_id", columnNames = {"name","user_account_id"})
        }
)
public class PlannedAction implements
        IPlannedStuff<PlannedAction>,
        IPlannedStuffContainerRequirements<PlannedActionContainer> {
    @Id
    @SequenceGenerator(
            name = "planned_action_sequence",
            sequenceName = "planned_action_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_action_sequence"
    )
    private Long id;
    private LocalTime startsAt;
    private LocalTime endsAt;
    @Column(
            nullable = false
    )
    private String name;
    private String description;
    private LocalTime remindBefore = null; //TODO: implement reminder feature
    @ManyToOne
    @JoinColumn(
            referencedColumnName = "id",
            name = "planned_action_container_id"
    )
    private PlannedActionContainer plannedActionContainer;
    @ManyToOne
    @JoinColumn(
            name = "user_account_id",
            referencedColumnName = "id"
    )

    private UserAccount userAccount;

    @Override
    @JsonIgnore
    public PlannedActionContainer getPlannedStuffContainer() {
        return getPlannedActionContainer();
    }

    @Override
    public void setPlannedStuffContainer(PlannedActionContainer container) {
        this.plannedActionContainer = container;
    }

    public PlannedAction(PlannedAction action) {
        this.id = action.id;
        this.startsAt = action.startsAt;
        this.endsAt = action.endsAt;
        this.name = action.name;
        this.description = action.description;
        this.remindBefore = action.remindBefore;
        this.plannedActionContainer = action.plannedActionContainer;
        this.userAccount = action.userAccount;
    }

    @Override
    public PlannedAction copyProperties(PlannedAction from){
        this.id = from.id;
        this.name = from.name;
        this.plannedActionContainer = from.plannedActionContainer;
        this.startsAt = from.startsAt;
        this.endsAt = from.endsAt;
        this.userAccount = from.userAccount;
        this.description= from.description;
        this.remindBefore = from.remindBefore;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlannedAction that = (PlannedAction) o;
        return Objects.equals(id, that.id) && Objects.equals(startsAt, that.startsAt) && Objects.equals(endsAt, that.endsAt) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(remindBefore, that.remindBefore) && Objects.equals(plannedActionContainer, that.plannedActionContainer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startsAt, endsAt, name, description, remindBefore, plannedActionContainer);
    }
}
