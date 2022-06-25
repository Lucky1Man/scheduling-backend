package com.online.scheduling.schedule.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.online.scheduling.schedule.implementations.json.serealize.UserSerializer;
import com.online.scheduling.user.entities.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "owner_id", columnNames = "owner_id")
        }
)
public class UserAccount {
    @Id
    @SequenceGenerator(
            name = "user_account_sequence",
            sequenceName = "user_account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_account_sequence"
    )
    private Long id;
    @OneToOne
    @JoinColumn(
            name = "owner_id",
            referencedColumnName = "user_id"
    )
    @JsonSerialize(using = UserSerializer.class)
    private User owner;
}
