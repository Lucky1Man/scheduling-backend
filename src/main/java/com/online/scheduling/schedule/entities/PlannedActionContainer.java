package com.online.scheduling.schedule.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity(name = "planned_action_container")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "planned_action_container_name", columnNames = {"name","user_account_id"})
        }
)
public class PlannedActionContainer implements
        IPlannedStuff<PlannedActionContainer> {
    @Id
    @SequenceGenerator(
            name = "planned_action_container_sequence",
            sequenceName = "planned_action_container_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_action_container_sequence"
    )
    private Long id;
    private String name;
    private String bgColor;
    @ManyToOne
    @JoinColumn(
            name = "user_account_id",
            referencedColumnName = "id"
    )
    private UserAccount userAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlannedActionContainer that = (PlannedActionContainer) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(bgColor, that.bgColor);
    }

    @Override
    public PlannedActionContainer copyProperties(PlannedActionContainer from) {
        this.id = from.id;
        this.name = from.name;
        this.bgColor = from.bgColor;
        this.userAccount = from.userAccount;
        return this;
    }


    @Override
    public boolean isBlank() {
        return IPlannedStuff.super.isBlank()
                && this.bgColor == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bgColor);
    }

    public PlannedActionContainer(PlannedActionContainer container) {
        this.id = container.id;
        this.name = container.name;
        this.bgColor = container.bgColor;
        this.userAccount = container.userAccount;
    }
}
