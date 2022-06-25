package com.online.scheduling.schedule.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.online.scheduling.schedule.implementations.json.serealize.UserAccountSerializer;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "planned_day_container")
@NoArgsConstructor
@AllArgsConstructor()
@ToString
@Getter
@Setter
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "planned_day_container_name", columnNames = {"name","user_account_id"})
        }
)
public class PlannedDayContainer implements IPlannedStuff<PlannedDayContainer> {
    @Id
    @SequenceGenerator(
            name = "planned_day_container_sequence",
            sequenceName = "planned_day_container_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_day_container_sequence"
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
        PlannedDayContainer that = (PlannedDayContainer) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(bgColor, that.bgColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bgColor);
    }
}
