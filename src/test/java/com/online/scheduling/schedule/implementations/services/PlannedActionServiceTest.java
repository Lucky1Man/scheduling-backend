package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.*;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.schedule.repositories.PlannedActionRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import com.online.scheduling.user.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.profiles.active=test")
class PlannedActionServiceTest {

    @Autowired
    PlannedActionService actionService;

    @MockBean
    PlannedActionRepository actionRepository;

    @MockBean
    PlannedActionContainerService actionContainerService;

    @MockBean
    UserAccountRepository userAccountRepository;

    User user = new User(
            1L,
            "Test firstName",
            "Test lastName",
            "test@gmail.com",
            "1",
            false,
            true);
    Authentication authentication = new UsernamePasswordAuthenticationToken("test@gmail.com", "1");
    UserAccount userAccount = new UserAccount(1L, user);

    PlannedActionContainer actionContainer = new PlannedActionContainer(null, "test action container", "white",null);

    PlannedAction action = new PlannedAction(
            null,
            LocalTime.of(0,0),
            LocalTime.of(2,0),
            "test name 1",
            "some description",
            null,
            actionContainer,
            null);

    @Test
    void save_id_not_found_exception(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(1L);
        action1.setUserAccount(userAccount);
        given(actionRepository.findById(action1.getId())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> actionService.save(authentication ,List.of(action1)));
    }

    @Test
    void save_name_taken_exception(){
        //given
        var savedAction = new PlannedAction(action);
        savedAction.setId(2L);
        savedAction.setUserAccount(userAccount);
        given(actionRepository.findByName(action.getName())).willReturn(Optional.of(savedAction));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionService.save(authentication, List.of(action)));
    }

    @Test
    void save_duplicates_found(){
        //given
        var action1 = new PlannedAction(action);
        var action2 = new PlannedAction(action);
        given(actionRepository.findByName(actionContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> actionService.save(authentication ,List.of(action1, action2)));
    }

    @Test
    void save_same_name_different_body_exception(){
        //given
        var action1 = new PlannedAction(action);
        action1.setDescription("111111");
        given(actionRepository.findByName(actionContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> actionService.save(authentication ,List.of(action, action1)));
    }

    @Test
    void save_invalid_name_exceptions(){
        //given
        var action1 = new PlannedAction(action);
        action1.setName(null);
        var action2 = new PlannedAction(action);
        action2.setName("11111");
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionService.save(authentication ,List.of(action1)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionService.save(authentication ,List.of(action2)));
    }

    @Test
    void save_invalid_startsAt_endsAt(){
        //given
        var action1 = new PlannedAction(action);
        action1.setStartsAt(LocalTime.of(2,0));
        action1.setEndsAt(LocalTime.of(0,0));
        action1.setUserAccount(userAccount);
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadTime.class, ()-> actionService.save(authentication, List.of(action1)));
    }

    @Test
    void save_user_not_confirmed(){
        //given
        var action1 = new PlannedAction(action);
        //then
        assertThrows(UserNotConfirmedException.class, ()-> actionService.save(authentication, List.of(action1)));
    }

    @Test
    void save_user_account_not_found(){
        //given
        var action1 = new PlannedAction(action);
        action1.setUserAccount(userAccount);
        given(actionRepository.findByName(action1.getName())).willReturn(Optional.empty());
        //then
        assertThrows(UserAccountNotFoundException.class, ()-> actionService.save(authentication, List.of(action1)));
    }

    @Test
    void save_successfully(){
        //given
        given(actionRepository.findByName(action.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertDoesNotThrow(()-> actionService.save(authentication, List.of(action)));
    }

    @Test
    void get_with_ids(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(1L);
        var action2 = new PlannedAction(action1);
        action2.setId(2L);
        action2.setName("test2");

        String email = authentication.getName();

        given(actionRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(action1,action2));
        //when
        List<PlannedAction> plannedActions = actionService.get(email, Set.of(1L));
        //then
        assertFalse(plannedActions.contains(action2));
    }

    @Test
    void get_without_ids(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(1L);
        var action2 = new PlannedAction(action1);
        action2.setId(2L);
        action2.setName("test2");

        String email = authentication.getName();

        given(actionRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(action1,action2));
        //when
        List<PlannedAction> plannedActions = actionService.get(email, null);
        //then
        assertTrue(plannedActions.containsAll(List.of(action1,action2)));
    }

    @Test
    void delete_non_existing_action_exception(){
        //given
        var ids = List.of(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> actionService.delete(ids));
    }

    @Test
    void delete_successfully() {
        //given
        var action1 = new PlannedAction(action);
        action1.setId(2L);
        action1.setUserAccount(userAccount);
        var action2 = new PlannedAction(action1);
        action2.setId(3L);
        action2.setName("test2");

        var ids = List.of(2L,3L);

        given(actionRepository.findById(2L)).willReturn(Optional.of(action1));
        given(actionRepository.findById(3L)).willReturn(Optional.of(action2));

        //when
        actionService.delete(ids);
        //then
        verify(actionRepository, times(1)).deleteAllById(any());
    }

    @Test
    void update_non_existing_action(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class,()-> actionService.update(authentication, List.of(action1)));
    }

    @Test
    void update_updating_name_already_taken(){
        //given
        var updatedAction = new PlannedAction(action); // 1 already in db
        updatedAction.setId(3L);
        updatedAction.setUserAccount(userAccount);

        var action1 = new PlannedAction(action); // 2 name conflict
        action1.setId(2L);
        action1.setName("updated name");
        action1.setUserAccount(userAccount);

        var action2 = new PlannedAction(action1); // 3 trying to update name in 1
        action2.setId(3L);

        given(actionRepository.findByName(action2.getName())).willReturn(Optional.of(action1));
        given(actionRepository.findById(3L)).willReturn(Optional.of(updatedAction));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionService.update(authentication, List.of(action2)));
    }

    @Test
    void update_invalid_name(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(2L);
        action1.setName("");
        action1.setUserAccount(userAccount);
        var action2 = new PlannedAction(action1);
        action2.setName("11111");
        given(actionRepository.findById(2L)).willReturn(Optional.of(action1));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionService.update(authentication, List.of(action1)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionService.update(authentication, List.of(action2)));
    }
    @Test
    void update_duplicates_found(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(2L);
        var action2 = new PlannedAction(action1);
        given(actionRepository.findById(2L)).willReturn(Optional.of(action1));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> actionService.update(authentication, List.of(action1, action2)));
    }

    @Test
    void update_same_name_different_body_exception(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(2L);
        action1.setDescription("1111");
        var action2 = new PlannedAction(action1);
        action2.setId(3L);
        action2.setDescription("2222");
        given(actionRepository.findById(2L)).willReturn(Optional.of(action1));
        given(actionRepository.findById(3L)).willReturn(Optional.of(action2));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> actionService.update(authentication, List.of(action1, action2)));
    }

    @Test
    void update_invalid_time_bounds(){
        //given
        var action1 = new PlannedAction(action);
        action1.setId(2L);
        action1.setStartsAt(LocalTime.of(4,0));
        action1.setEndsAt(LocalTime.of(2,0));
        given(actionRepository.findById(2L)).willReturn(Optional.of(action));
        //then
        assertThrows(ValidationExceptionGivenBadTime.class, ()-> actionService.update(authentication, List.of(action1)));
    }

    @Test
    void update_successfully(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(2L);
        container.setBgColor("white");
        var action1 = new PlannedAction(action);
        action1.setId(2L);
        action1.setDescription("white");
        action1.setName("updated name");
        action1.setPlannedActionContainer(container);
        action1.setStartsAt(LocalTime.of(2,0));
        action1.setEndsAt(LocalTime.of(4,0));
        given(actionRepository.findById(2L)).willReturn(Optional.of(action));
        given(actionRepository.getById(2L)).willReturn(action);
        //then
        assertDoesNotThrow(()-> actionService.update(authentication ,List.of(action1)));
    }


}