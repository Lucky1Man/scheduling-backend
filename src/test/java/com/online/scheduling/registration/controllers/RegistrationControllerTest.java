package com.online.scheduling.registration.controllers;

import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;
import com.online.scheduling.exceptions.InvalidConfirmationTokenException;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.exceptions.UserAlreadyConfirmedException;
import com.online.scheduling.registration.implementations.services.RegistrationRequestServiceImpl;
import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.registration.tokens.ConfirmationToken;
import com.online.scheduling.registration.tokens.ConfirmationTokenRepository;
import com.online.scheduling.registration.tokens.ConfirmationTokenService;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import com.online.scheduling.user.entities.User;
import com.online.scheduling.user.repositories.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.profiles.active=test")
class RegistrationControllerTest {

    @Autowired
    RegistrationController registrationController;
    @Autowired
    @SpyBean
    RegistrationRequestServiceImpl registrationRequestService;
    @MockBean
    ConfirmationTokenService confirmationTokenService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ConfirmationTokenRepository confirmationTokenRepository;
    @MockBean
    UserAccountRepository userAccountRepository;

    RegistrationRequest REQ = new RegistrationRequest("Test first name 1", "Test last name 1", "testemail1@gmail.com", "1");

    User user = User.builder()
            .id(1L)
            .firstName(REQ.getFirstName())
            .lastName(REQ.getLastName())
            .enabled(false)
            .email(REQ.getEmail())
            .password(REQ.getPassword())
            .build();

    String stringToken = "test";
    ConfirmationToken confirmationToken = new ConfirmationToken(
            stringToken,
            LocalDateTime.of(LocalDate.of(2022, 1,1), LocalTime.of(0,0)),
            LocalDateTime.of(LocalDate.of(999999999, 1,1), LocalTime.of(0,0)),
            user);



    @Test
    @Order(1)
    void register_invalid_first_name_exceptions() {
        //given
        var reqInvalidFirstName_blank = new RegistrationRequest(REQ);
        reqInvalidFirstName_blank.setFirstName(null);
        var reqInvalidFirstName_noLetters = new RegistrationRequest(REQ);
        reqInvalidFirstName_noLetters.setFirstName("1111111");
        //expect
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidFirstName_blank));
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidFirstName_noLetters));
    }

    @Test
    @Order(2)
    void register_invalid_last_name_exception(){
        //given
        var reqInvalidLastName_blank = new RegistrationRequest(REQ);
        reqInvalidLastName_blank.setLastName(null);
        var reqInvalidLastName_noLetters = new RegistrationRequest(REQ);
        reqInvalidLastName_noLetters.setLastName("1111111");
        //expect
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidLastName_blank));
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidLastName_noLetters));
    }

    @Test
    @Order(3)
    void register_invalid_email_exception(){
        //given
        var reqInvalidEmail_blank = new RegistrationRequest(REQ);
        reqInvalidEmail_blank.setEmail(null);
        var reqInvalidEmail_Invalid1 = new RegistrationRequest(REQ);
        reqInvalidEmail_Invalid1.setEmail("@gmail.com"); //missing *@gmail.com
        var reqInvalidEmail_Invalid2 = new RegistrationRequest(REQ);
        reqInvalidEmail_Invalid2.setEmail("testgmail.com"); //missing test*gmail.com
        var reqInvalidEmail_Invalid3 = new RegistrationRequest(REQ);
        reqInvalidEmail_Invalid3.setEmail("test@.com"); //missing test@*.com
        var reqInvalidEmail_Invalid4 = new RegistrationRequest(REQ);
        reqInvalidEmail_Invalid4.setEmail("test@gmail."); //missing test@gmail.*
        //then
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidEmail_blank));
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidEmail_Invalid1));
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidEmail_Invalid2));
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidEmail_Invalid3));
        assertThrows(RegistrationInvalidDataException.class, ()->registrationController.register(reqInvalidEmail_Invalid4));
    }

    @Test
    @Order(4)
    void register_successfully() {
        //given
        given(userRepository.findByEmail(REQ.getEmail())).willReturn(Optional.empty());
        doNothing().when(registrationRequestService).confirmToken(any(String.class));
        //then
        assertDoesNotThrow(()->registrationController.register(REQ));
    }

    @Test
    @Order(5)
    void register_email_taken_exception() {
        //given
        given(userRepository.findByEmail(REQ.getEmail())).willReturn(Optional.of(user));
        //then
        assertThrows(EmailAlreadyRegisteredException.class, ()->registrationController.register(REQ));
    }
    @Test
    @Order(6)
    void confirm_unsuccessfully() {
        //given
        String localToken = "1";
        given(confirmationTokenRepository.findByToken(localToken)).willReturn(Optional.empty());

        //expect
        assertThrows(InvalidConfirmationTokenException.class ,()->registrationController.confirm(localToken));
    }

    @Test
    @Order(7)
    void confirm_already_confirmed_exception(){
        //given
        ConfirmationToken confirmationToken_alreadyConfirmed = new ConfirmationToken(confirmationToken);
        confirmationToken_alreadyConfirmed.setConfirmedAt(LocalDateTime.of(LocalDate.of(2022, 1,2), LocalTime.of(0,0)));
        given(confirmationTokenRepository.findByToken(stringToken)).willReturn(Optional.of(confirmationToken_alreadyConfirmed));
        //then
        assertThrows(InvalidConfirmationTokenException.class, ()->registrationController.confirm(stringToken));
    }

    @Test
    @Order(8)
    void confirm_expired_exception(){
        //given
        ConfirmationToken confirmationToken_expired = new ConfirmationToken(confirmationToken);
        confirmationToken_expired.setExpiresAt(LocalDateTime.of(LocalDate.of(2020, 1,1), LocalTime.of(0,0)));
        given(confirmationTokenRepository.findByToken(stringToken)).willReturn(Optional.of(confirmationToken_expired));
        //then
        assertThrows(InvalidConfirmationTokenException.class, ()->registrationController.confirm(stringToken));
    }

    @Test
    @Order(9)
    void confirm_user_already_confirmed(){
        //given
        given(confirmationTokenService.getToken(stringToken)).willReturn(Optional.of(confirmationToken));
        ConfirmationToken alreadyConfirmed = new ConfirmationToken(confirmationToken);
        alreadyConfirmed.setConfirmedAt(LocalDateTime.of(LocalDate.of(2020, 1,1), LocalTime.of(0,0)));
        given(confirmationTokenService.findByUserId(user.getId())).willReturn(List.of(alreadyConfirmed));
        //expect
        assertThrows(UserAlreadyConfirmedException.class, ()->registrationController.confirm(stringToken));
    }

    @Test
    @Order(10)
    void confirm_successfully() {
        //given
        given(confirmationTokenService.getToken(stringToken)).willReturn(Optional.of(confirmationToken));
        //expect
        assertDoesNotThrow(()->registrationController.confirm(stringToken));
    }
}
