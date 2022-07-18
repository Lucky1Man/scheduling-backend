package com.online.scheduling.schedule.entities;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "schedule_container_name", columnNames = {"name","user_account_id"})
        }
)
public class ScheduleContainer implements IPlannedStuff<ScheduleContainer> {
    @Id
    @SequenceGenerator(
            name = "schedule_container_sequence",
            sequenceName = "schedule_container_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "schedule_container_sequence"
    )
    private Long id;
    @Column(
            nullable = false
    )
    private String name;
    private String bgColor;
    @ManyToOne
    @JoinColumn(
            name = "user_account_id",
            referencedColumnName = "id"
    )
    private UserAccount userAccount;

    public ScheduleContainer(ScheduleContainer container) {
        this.id = container.id;
        this.name = container.name;
        this.bgColor = container.bgColor;
        this.userAccount = container.userAccount;
    }

    @Override
    public ScheduleContainer copyProperties(ScheduleContainer from) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleContainer that = (ScheduleContainer) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(bgColor, that.bgColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bgColor);
    }
}
