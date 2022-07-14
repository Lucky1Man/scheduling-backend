package com.online.scheduling.registration.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public RegistrationRequest(RegistrationRequest request) {
        this.firstName = request.firstName;
        this.lastName = request.lastName;
        this.email = request.email;
        this.password = request.password;
    }
}
