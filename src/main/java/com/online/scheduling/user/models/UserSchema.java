package com.online.scheduling.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSchema {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
