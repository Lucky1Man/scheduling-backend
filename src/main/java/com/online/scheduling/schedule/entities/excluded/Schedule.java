package com.online.scheduling.schedule.entities.excluded;

import com.online.scheduling.user.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Embeddable
@ToString(
        exclude = "members"
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {
    private String name;
    @OneToOne
    private PlansContainer plans;
    @OneToOne
    private User owner;
    @ManyToMany
    private List<User> members;
}
