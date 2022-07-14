package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.*;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.schedule.repositories.ScheduleContainerRepository;
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
class ScheduleContainerServiceTest {
    @Autowired
    ScheduleContainerService scheduleContainerService;
    @MockBean
    ScheduleContainerRepository scheduleContainerRepository;
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
    ScheduleContainer scheduleContainer = new ScheduleContainer(null, "test action container", "white",null);

    @Test
    void save_id_not_found_exception(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(1L);
        container.setUserAccount(userAccount);
        given(scheduleContainerRepository.findById(container.getId())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> scheduleContainerService.save(authentication , List.of(container)));
    }

    @Test
    void save_name_taken_exception(){
        //given
        var savedContainer = new ScheduleContainer(scheduleContainer);
        savedContainer.setId(1L);
        savedContainer.setUserAccount(userAccount);
        given(scheduleContainerRepository.findByName(scheduleContainer.getName())).willReturn(Optional.of(savedContainer));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> scheduleContainerService.save(authentication ,List.of(scheduleContainer)));
    }

    @Test
    void save_duplicates_found(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        var container2 = new ScheduleContainer(scheduleContainer);
        given(scheduleContainerRepository.findByName(scheduleContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> scheduleContainerService.save(authentication ,List.of(container, container2)));
    }

    @Test
    void save_same_name_different_body_exception(){
        //given
        var secondContainer = new ScheduleContainer(scheduleContainer);
        secondContainer.setBgColor("blue");
        given(scheduleContainerRepository.findByName(scheduleContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> scheduleContainerService.save(authentication ,List.of(scheduleContainer, secondContainer)));
    }

    @Test
    void save_invalid_name_exceptions(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setName(null);
        var container2 = new ScheduleContainer(scheduleContainer);
        container2.setName("11111");
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> scheduleContainerService.save(authentication ,List.of(container)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> scheduleContainerService.save(authentication ,List.of(container2)));
    }

    @Test
    void save_user_not_confirmed(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        //then
        assertThrows(UserNotConfirmedException.class, ()-> scheduleContainerService.save(authentication, List.of(container)));
    }
    @Test
    void save_user_account_not_found(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setUserAccount(userAccount);
        given(scheduleContainerRepository.findByName(container.getName())).willReturn(Optional.empty());
        //then
        assertThrows(UserAccountNotFoundException.class, ()-> scheduleContainerService.save(authentication, List.of(container)));
    }

    @Test
    void save_successfully() {
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(1L);

        given(scheduleContainerRepository.findByName(container.getName())).willReturn(Optional.empty());
        given(scheduleContainerRepository.saveAll(List.of(scheduleContainer))).willReturn(List.of(scheduleContainer));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //when
        List<ScheduleContainer> saved = scheduleContainerService.save(authentication, List.of(scheduleContainer));
        //then
        assertDoesNotThrow(()-> scheduleContainerService.save(authentication, List.of(scheduleContainer)));
        assertEquals(userAccount, saved.get(0).getUserAccount());
    }

    @Test
    void get_with_ids() {
        //given
        var container1 = new ScheduleContainer(scheduleContainer);
        container1.setId(1L);
        container1.setUserAccount(userAccount);
        var container2 = new ScheduleContainer(container1);
        container2.setId(2L);
        container2.setName("test2");


        String email = authentication.getName();

        given(scheduleContainerRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(container1,container2));
        //when
        List<ScheduleContainer> plannedActionContainers = scheduleContainerService.get(email, Set.of(1L));
        //then
        assertFalse(plannedActionContainers.contains(container2));
    }

    @Test
    void get_without_ids(){
        //given
        var container1 = new ScheduleContainer(scheduleContainer);
        container1.setId(1L);
        container1.setUserAccount(userAccount);
        var container2 = new ScheduleContainer(container1);
        container2.setId(2L);
        container2.setName("test2");
        String email = "test@gmail.com";

        given(scheduleContainerRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(container1,container2));
        //when
        List<ScheduleContainer> plannedActionContainers = scheduleContainerService.get(email, null);
        //then
        assertTrue(plannedActionContainers.containsAll(List.of(container1,container2)));
    }

    @Test
    void delete_try_to_delete_basic_container(){
        //given
        var ids = List.of(1L, 2L);
        //then
        assertThrows(UnableToExecuteQueryException.class, ()-> scheduleContainerService.delete(ids));
    }

    @Test
    void delete_non_existing_container_exception(){
        //given
        var ids = List.of(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> scheduleContainerService.delete(ids));
    }

    @Test
    void delete_successfully() {
        //given
        var container1 = new ScheduleContainer(scheduleContainer);
        container1.setId(2L);
        container1.setUserAccount(userAccount);
        var container2 = new ScheduleContainer(container1);
        container2.setId(3L);
        container2.setName("test2");


        var ids = List.of(2L,3L);

        given(scheduleContainerRepository.findById(2L)).willReturn(Optional.of(container1));
        given(scheduleContainerRepository.findById(3L)).willReturn(Optional.of(container2));

        //when
        scheduleContainerService.delete(ids);
        //then
        verify(scheduleContainerRepository, times(1)).deleteAllById(any());
    }

    @Test
    void update_try_updating_basic_container(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(1L);
        //then
        assertThrows(UnableToExecuteQueryException.class,()-> scheduleContainerService.update(List.of(container)));
    }

    @Test
    void update_non_existing_container(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class,()-> scheduleContainerService.update(List.of(container)));
    }

    @Test
    void update_updating_name_already_taken(){
        //given
        var updatedContainer = new ScheduleContainer(scheduleContainer);
        updatedContainer.setId(3L);
        updatedContainer.setUserAccount(userAccount);
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(2L);
        container.setName("updated name");
        container.setUserAccount(userAccount);
        var container2 = new ScheduleContainer(container);
        container2.setId(3L);
        given(scheduleContainerRepository.findByName(container2.getName())).willReturn(Optional.of(container));
        given(scheduleContainerRepository.findById(3L)).willReturn(Optional.of(updatedContainer));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> scheduleContainerService.update(List.of(container2)));
    }

    @Test
    void update_invalid_name(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(2L);
        container.setName("");
        container.setUserAccount(userAccount);
        var container2 = new ScheduleContainer(container);
        container.setId(2L);
        container2.setName("11111");
        given(scheduleContainerRepository.findById(2L)).willReturn(Optional.of(container));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> scheduleContainerService.update(List.of(container)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> scheduleContainerService.update(List.of(container2)));

    }
    @Test
    void update_duplicates_found(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(2L);
        var container2 = new ScheduleContainer(container);
        given(scheduleContainerRepository.findById(2L)).willReturn(Optional.of(container));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> scheduleContainerService.update(List.of(container, container2)));
    }

    @Test
    void update_same_name_different_body_exception(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(2L);
        container.setBgColor("blue");
        var container2 = new ScheduleContainer(container);
        container2.setId(3L);
        container2.setBgColor("white");
        given(scheduleContainerRepository.findById(2L)).willReturn(Optional.of(container));
        given(scheduleContainerRepository.findById(3L)).willReturn(Optional.of(container2));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> scheduleContainerService.update(List.of(container, container2)));
    }

    @Test
    void update_successfully(){
        //given
        var container = new ScheduleContainer(scheduleContainer);
        container.setId(2L);
        container.setBgColor("white");
        container.setName("updated name");
        given(scheduleContainerRepository.findById(2L)).willReturn(Optional.of(scheduleContainer));
        given(scheduleContainerRepository.getById(2L)).willReturn(scheduleContainer);
        //then
        assertDoesNotThrow(()-> scheduleContainerService.update(List.of(container)));
    }
}