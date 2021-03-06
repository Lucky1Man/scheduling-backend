package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.*;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.schedule.repositories.PlannedActionContainerRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import com.online.scheduling.user.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.profiles.active=test")
class PlannedActionContainerServiceTest {

    @Autowired
    PlannedActionContainerService actionContainerService;

    @MockBean
    PlannedActionContainerRepository actionContainerRepository;
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

    @Test
    void save_id_not_found_exception(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(1L);
        container.setUserAccount(userAccount);
        given(actionContainerRepository.findById(container.getId())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> actionContainerService.save(authentication ,List.of(container)));
    }

    @Test
    void save_name_taken_exception(){
        //given
        var savedContainer = new PlannedActionContainer(actionContainer);
        savedContainer.setId(1L);
        savedContainer.setUserAccount(userAccount);
        given(actionContainerRepository.findByName(actionContainer.getName())).willReturn(Optional.of(savedContainer));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionContainerService.save(authentication ,List.of(actionContainer)));
    }

    @Test
    void save_duplicates_found(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        var container2 = new PlannedActionContainer(actionContainer);
        given(actionContainerRepository.findByName(actionContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> actionContainerService.save(authentication ,List.of(container, container2)));
    }

    @Test
    void save_same_name_different_body_exception(){
        //given
        var secondContainer = new PlannedActionContainer(actionContainer);
        secondContainer.setBgColor("blue");
        given(actionContainerRepository.findByName(actionContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> actionContainerService.save(authentication ,List.of(actionContainer, secondContainer)));
    }

    @Test
    void save_invalid_name_exceptions(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setName(null);
        var container2 = new PlannedActionContainer(actionContainer);
        container2.setName("11111");
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionContainerService.save(authentication ,List.of(container)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionContainerService.save(authentication ,List.of(container2)));
    }

    @Test
    void save_user_not_confirmed(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        //then
        assertThrows(UserNotConfirmedException.class, ()-> actionContainerService.save(authentication, List.of(container)));
    }
    @Test
    void save_user_account_not_found(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setUserAccount(userAccount);
        given(actionContainerRepository.findByName(container.getName())).willReturn(Optional.empty());
        //then
        assertThrows(UserAccountNotFoundException.class, ()-> actionContainerService.save(authentication, List.of(container)));
    }

    @Test
    void save_successfully() {
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(1L);

        given(actionContainerRepository.findByName(container.getName())).willReturn(Optional.empty());
        given(actionContainerRepository.saveAll(List.of(actionContainer))).willReturn(List.of(actionContainer));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //when
        List<PlannedActionContainer> saved = actionContainerService.save(authentication, List.of(actionContainer));
        //then
        assertDoesNotThrow(()-> actionContainerService.save(authentication, List.of(actionContainer)));
        assertEquals(userAccount, saved.get(0).getUserAccount());
    }

    @Test
    void get_with_ids() {
        //given
        var container1 = new PlannedActionContainer(actionContainer);
        container1.setId(1L);
        container1.setUserAccount(userAccount);
        var container2 = new PlannedActionContainer(container1);
        container2.setId(2L);
        container2.setName("test2");


        String email = authentication.getName();

        given(actionContainerRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(container1,container2));
        //when
        List<PlannedActionContainer> plannedActionContainers = actionContainerService.get(email, Set.of(1L));
        //then
        assertFalse(plannedActionContainers.contains(container2));
    }

    @Test
    void get_without_ids(){
        //given
        var container1 = new PlannedActionContainer(actionContainer);
        container1.setId(1L);
        container1.setUserAccount(userAccount);
        var container2 = new PlannedActionContainer(container1);
        container2.setId(2L);
        container2.setName("test2");
        String email = "test@gmail.com";

        given(actionContainerRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(container1,container2));
        //when
        List<PlannedActionContainer> plannedActionContainers = actionContainerService.get(email, null);
        //then
        assertTrue(plannedActionContainers.containsAll(List.of(container1,container2)));
    }

    @Test
    void delete_try_to_delete_basic_container(){
        //given
        var ids = List.of(1L, 2L);
        //then
        assertThrows(UnableToExecuteQueryException.class, ()-> actionContainerService.delete(ids));
    }

    @Test
    void delete_non_existing_container_exception(){
        //given
        var ids = List.of(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> actionContainerService.delete(ids));
    }

    @Test
    void delete_try_deleting_stuff_that_is_involved_in_another_stuff(){
        //given
        var ids = List.of(2L);
        var container1 = new PlannedActionContainer(actionContainer);
        container1.setId(2L);
        var plannedAction1 = new PlannedAction();
        plannedAction1.setId(1L);
        plannedAction1.setName("some name");
        plannedAction1.setUserAccount(userAccount);
        plannedAction1.setPlannedActionContainer(container1);
        var container2 = new PlannedActionContainer(actionContainer);
        container2.setId(3L);
        container2.setName("different");
        var plannedAction2 = new PlannedAction();
        plannedAction2.setId(2L);
        plannedAction2.setName("some name 2");
        plannedAction2.setUserAccount(userAccount);
        plannedAction2.setPlannedActionContainer(container2);

        given(actionContainerRepository.getPlannedStuffHolders(ids.get(0))).willReturn(List.of(plannedAction1));
        given(actionContainerRepository.findById(2L)).willReturn(Optional.of(container1));
        //when
        var result = actionContainerService.delete(ids);
        //then
        assertTrue(result.get(ids.get(0)).contains(plannedAction1));
        assertFalse(result.get(ids.get(0)).contains(plannedAction2));
        verify(actionContainerRepository, times(0)).deleteAllById(any());
    }

    @Test
    void delete_successfully() {
        //given
        var container1 = new PlannedActionContainer(actionContainer);
        container1.setId(2L);
        container1.setUserAccount(userAccount);
        var container2 = new PlannedActionContainer(container1);
        container2.setId(3L);
        container2.setName("test2");


        var ids = List.of(2L,3L);

        given(actionContainerRepository.findById(2L)).willReturn(Optional.of(container1));
        given(actionContainerRepository.findById(3L)).willReturn(Optional.of(container2));

        //when
        actionContainerService.delete(ids);
        //then
        verify(actionContainerRepository, times(1)).deleteAllById(any());
    }

    @Test
    void update_try_updating_basic_container(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(1L);
        //then
        assertThrows(UnableToExecuteQueryException.class,()-> actionContainerService.update(List.of(container)));
    }

    @Test
    void update_non_existing_container(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class,()-> actionContainerService.update(List.of(container)));
    }

    @Test
    void update_updating_name_already_taken(){
        //given
        var updatedContainer = new PlannedActionContainer(actionContainer);
        updatedContainer.setId(3L);
        updatedContainer.setUserAccount(userAccount);
        var container = new PlannedActionContainer(actionContainer);
        container.setId(2L);
        container.setName("updated name");
        container.setUserAccount(userAccount);
        var container2 = new PlannedActionContainer(container);
        container2.setId(3L);
        given(actionContainerRepository.findByName(container2.getName())).willReturn(Optional.of(container));
        given(actionContainerRepository.findById(3L)).willReturn(Optional.of(updatedContainer));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionContainerService.update(List.of(container2)));
    }

    @Test
    void update_invalid_name(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(2L);
        container.setName("");
        container.setUserAccount(userAccount);
        var container2 = new PlannedActionContainer(container);
        container.setId(2L);
        container2.setName("11111");
        given(actionContainerRepository.findById(2L)).willReturn(Optional.of(container));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionContainerService.update(List.of(container)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> actionContainerService.update(List.of(container2)));

    }
    @Test
    void update_duplicates_found(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(2L);
        var container2 = new PlannedActionContainer(container);
        given(actionContainerRepository.findById(2L)).willReturn(Optional.of(container));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> actionContainerService.update(List.of(container, container2)));
    }

    @Test
    void update_same_name_different_body_exception(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(2L);
        container.setBgColor("blue");
        var container2 = new PlannedActionContainer(container);
        container2.setId(3L);
        container2.setBgColor("white");
        given(actionContainerRepository.findById(2L)).willReturn(Optional.of(container));
        given(actionContainerRepository.findById(3L)).willReturn(Optional.of(container2));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> actionContainerService.update(List.of(container, container2)));
    }

    @Test
    void update_successfully(){
        //given
        var container = new PlannedActionContainer(actionContainer);
        container.setId(2L);
        container.setBgColor("white");
        container.setName("updated name");
        given(actionContainerRepository.findById(2L)).willReturn(Optional.of(actionContainer));
        given(actionContainerRepository.getById(2L)).willReturn(actionContainer);
        //then
        assertDoesNotThrow(()-> actionContainerService.update(List.of(container)));
    }
}