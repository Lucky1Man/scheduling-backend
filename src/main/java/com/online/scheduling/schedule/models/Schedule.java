package com.online.scheduling.schedule.models;

import com.online.scheduling.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Embeddable
@Data
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
