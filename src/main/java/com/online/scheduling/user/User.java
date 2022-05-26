package com.online.scheduling.user;

import com.online.scheduling.registration.tokens.ConfirmationToken;
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
@ToString
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
    private boolean enabled = false;
    public User(){}
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
