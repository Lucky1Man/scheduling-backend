package com.online.scheduling.user.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.scheduling.registration.tokens.ConfirmationToken;
import com.online.scheduling.schedule.entities.UserAccount;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = @UniqueConstraint(
            name = "user_email_id",
            columnNames = "user_email"
        )
)
@Getter
@ToString(exclude = "userAccount")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    @Column(name = "user_id")
    private Long id;
    @Column(
            name = "user_first_name",
            nullable = false
    )
    private String firstName;
    @Column(
            name = "user_last_name",
            nullable = false
    )
    private String lastName;
    @Column(
            name = "user_email",
            nullable = false
    )
    private String email;
    @Column(
            name = "user_password",
            nullable = false
    )
    private String password;
    @Column(
            name = "user_locked",
            nullable = false
    )
    private boolean locked = false;
    @Column(
            name = "user_enabled",
            nullable = false
    )
    private boolean enabled;
    @OneToOne
    @JsonIgnore
    private UserAccount userAccount;
}
