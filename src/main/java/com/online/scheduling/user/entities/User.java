package com.online.scheduling.user.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.online.scheduling.schedule.implementations.json.serealize.UserSerializer;
import com.online.scheduling.user.models.UserSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(implementation = UserSchema.class)
@JsonSerialize(using = UserSerializer.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return locked == user.locked && enabled == user.enabled && Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, locked, enabled);
    }
}
