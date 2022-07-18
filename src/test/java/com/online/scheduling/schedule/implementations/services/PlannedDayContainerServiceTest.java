package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.*;
import com.online.scheduling.schedule.entities.*;
import com.online.scheduling.schedule.repositories.PlannedDayContainerRepository;
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
class PlannedDayContainerServiceTest {

    @Autowired
    PlannedDayContainerService dayContainerService;
    @MockBean
    PlannedDayContainerRepository dayContainerRepository;
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
    PlannedDayContainer dayContainer = new PlannedDayContainer(null, "test action container", "white",null);

    @Test
    void save_id_not_found_exception(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(1L);
        container.setUserAccount(userAccount);
        given(dayContainerRepository.findById(container.getId())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> dayContainerService.save(authentication , List.of(container)));
    }

    @Test
    void save_name_taken_exception(){
        //given
        var savedContainer = new PlannedDayContainer(dayContainer);
        savedContainer.setId(1L);
        savedContainer.setUserAccount(userAccount);
        given(dayContainerRepository.findByName(dayContainer.getName())).willReturn(Optional.of(savedContainer));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> dayContainerService.save(authentication ,List.of(dayContainer)));
    }

    @Test
    void save_duplicates_found(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        var container2 = new PlannedDayContainer(dayContainer);
        given(dayContainerRepository.findByName(dayContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> dayContainerService.save(authentication ,List.of(container, container2)));
    }

    @Test
    void save_same_name_different_body_exception(){
        //given
        var secondContainer = new PlannedDayContainer(dayContainer);
        secondContainer.setBgColor("blue");
        given(dayContainerRepository.findByName(dayContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> dayContainerService.save(authentication ,List.of(dayContainer, secondContainer)));
    }

    @Test
    void save_invalid_name_exceptions(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setName(null);
        var container2 = new PlannedDayContainer(dayContainer);
        container2.setName("11111");
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> dayContainerService.save(authentication ,List.of(container)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> dayContainerService.save(authentication ,List.of(container2)));
    }

    @Test
    void save_user_not_confirmed(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        //then
        assertThrows(UserNotConfirmedException.class, ()-> dayContainerService.save(authentication, List.of(container)));
    }
    @Test
    void save_user_account_not_found(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setUserAccount(userAccount);
        given(dayContainerRepository.findByName(container.getName())).willReturn(Optional.empty());
        //then
        assertThrows(UserAccountNotFoundException.class, ()-> dayContainerService.save(authentication, List.of(container)));
    }

    @Test
    void save_successfully() {
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(1L);

        given(dayContainerRepository.findByName(container.getName())).willReturn(Optional.empty());
        given(dayContainerRepository.saveAll(List.of(dayContainer))).willReturn(List.of(dayContainer));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //when
        List<PlannedDayContainer> saved = dayContainerService.save(authentication, List.of(dayContainer));
        //then
        assertDoesNotThrow(()-> dayContainerService.save(authentication, List.of(dayContainer)));
        assertEquals(userAccount, saved.get(0).getUserAccount());
    }

    @Test
    void get_with_ids() {
        //given
        var container1 = new PlannedDayContainer(dayContainer);
        container1.setId(1L);
        container1.setUserAccount(userAccount);
        var container2 = new PlannedDayContainer(container1);
        container2.setId(2L);
        container2.setName("test2");


        String email = authentication.getName();

        given(dayContainerRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(container1,container2));
        //when
        List<PlannedDayContainer> plannedActionContainers = dayContainerService.get(email, Set.of(1L));
        //then
        assertFalse(plannedActionContainers.contains(container2));
    }

    @Test
    void get_without_ids(){
        //given
        var container1 = new PlannedDayContainer(dayContainer);
        container1.setId(1L);
        container1.setUserAccount(userAccount);
        var container2 = new PlannedDayContainer(container1);
        container2.setId(2L);
        container2.setName("test2");
        String email = "test@gmail.com";

        given(dayContainerRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(container1,container2));
        //when
        List<PlannedDayContainer> plannedActionContainers = dayContainerService.get(email, null);
        //then
        assertTrue(plannedActionContainers.containsAll(List.of(container1,container2)));
    }

    @Test
    void delete_try_to_delete_basic_container(){
        //given
        var ids = List.of(1L, 2L);
        //then
        assertThrows(UnableToExecuteQueryException.class, ()-> dayContainerService.delete(ids));
    }

    @Test
    void delete_non_existing_container_exception(){
        //given
        var ids = List.of(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, ()-> dayContainerService.delete(ids));
    }

    @Test
    void delete_try_deleting_stuff_that_is_involved_in_another_stuff(){
        //given
        var ids = List.of(2L);
        var container1 = new PlannedDayContainer(dayContainer);
        container1.setId(2L);
        var day1 = new PlannedDay();
        day1.setId(1L);
        day1.setName("some name");
        day1.setUserAccount(userAccount);
        day1.setPlannedDayContainer(container1);
        var container2 = new PlannedDayContainer(dayContainer);
        container2.setId(3L);
        container2.setName("different");
        var day2 = new PlannedDay();
        day2.setId(2L);
        day2.setName("some name 2");
        day2.setUserAccount(userAccount);
        day2.setPlannedDayContainer(container2);

        given(dayContainerRepository.getPlannedStuffHolders(ids.get(0))).willReturn(List.of(day1));
        given(dayContainerRepository.findById(2L)).willReturn(Optional.of(container1));
        //when
        var result = dayContainerService.delete(ids);
        //then
        assertTrue(result.get(ids.get(0)).contains(day1));
        assertFalse(result.get(ids.get(0)).contains(day2));
        verify(dayContainerRepository, times(0)).deleteAllById(any());
    }

    @Test
    void delete_successfully() {
        //given
        var container1 = new PlannedDayContainer(dayContainer);
        container1.setId(2L);
        container1.setUserAccount(userAccount);
        var container2 = new PlannedDayContainer(container1);
        container2.setId(3L);
        container2.setName("test2");


        var ids = List.of(2L,3L);

        given(dayContainerRepository.findById(2L)).willReturn(Optional.of(container1));
        given(dayContainerRepository.findById(3L)).willReturn(Optional.of(container2));

        //when
        dayContainerService.delete(ids);
        //then
        verify(dayContainerRepository, times(1)).deleteAllById(any());
    }

    @Test
    void update_try_updating_basic_container(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(1L);
        //then
        assertThrows(UnableToExecuteQueryException.class,()-> dayContainerService.update(List.of(container)));
    }

    @Test
    void update_non_existing_container(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class,()-> dayContainerService.update(List.of(container)));
    }

    @Test
    void update_updating_name_already_taken(){
        //given
        var updatedContainer = new PlannedDayContainer(dayContainer);
        updatedContainer.setId(3L);
        updatedContainer.setUserAccount(userAccount);
        var container = new PlannedDayContainer(dayContainer);
        container.setId(2L);
        container.setName("updated name");
        container.setUserAccount(userAccount);
        var container2 = new PlannedDayContainer(container);
        container2.setId(3L);
        given(dayContainerRepository.findByName(container2.getName())).willReturn(Optional.of(container));
        given(dayContainerRepository.findById(3L)).willReturn(Optional.of(updatedContainer));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> dayContainerService.update(List.of(container2)));
    }

    @Test
    void update_invalid_name(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(2L);
        container.setName("");
        container.setUserAccount(userAccount);
        var container2 = new PlannedDayContainer(container);
        container.setId(2L);
        container2.setName("11111");
        given(dayContainerRepository.findById(2L)).willReturn(Optional.of(container));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, ()-> dayContainerService.update(List.of(container)));
        assertThrows(ValidationExceptionGivenBadName.class, ()-> dayContainerService.update(List.of(container2)));

    }
    @Test
    void update_duplicates_found(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(2L);
        var container2 = new PlannedDayContainer(container);
        given(dayContainerRepository.findById(2L)).willReturn(Optional.of(container));
        //then
        assertThrows(DuplicatesFoundException.class, ()-> dayContainerService.update(List.of(container, container2)));
    }

    @Test
    void update_same_name_different_body_exception(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(2L);
        container.setBgColor("blue");
        var container2 = new PlannedDayContainer(container);
        container2.setId(3L);
        container2.setBgColor("white");
        given(dayContainerRepository.findById(2L)).willReturn(Optional.of(container));
        given(dayContainerRepository.findById(3L)).willReturn(Optional.of(container2));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> dayContainerService.update(List.of(container, container2)));
    }

    @Test
    void update_successfully(){
        //given
        var container = new PlannedDayContainer(dayContainer);
        container.setId(2L);
        container.setBgColor("white");
        container.setName("updated name");
        given(dayContainerRepository.findById(2L)).willReturn(Optional.of(dayContainer));
        given(dayContainerRepository.getById(2L)).willReturn(dayContainer);
        //then
        assertDoesNotThrow(()-> dayContainerService.update(List.of(container)));
    }

}