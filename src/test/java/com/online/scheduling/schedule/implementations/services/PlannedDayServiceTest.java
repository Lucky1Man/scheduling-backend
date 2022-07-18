package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.*;
import com.online.scheduling.schedule.entities.*;
import com.online.scheduling.schedule.repositories.PlannedDayRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import com.online.scheduling.user.entities.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.profiles.active=test")
class PlannedDayServiceTest {

    @Autowired
    PlannedDayService dayService;

    @MockBean
    PlannedDayRepository dayRepository;
    @MockBean
    UserAccountRepository userAccountRepository;
    @MockBean
    PlannedDayContainerService dayContainerService;
    @MockBean
    PlannedActionService actionService;

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

    PlannedDayContainer dayContainer = new PlannedDayContainer(null, "test day container", "white", null);
    PlannedDay day = new PlannedDay(
            null,
            LocalDate.of(2022, 1,1),
            DayOfWeek.TUESDAY,
            "test day name",
            Lists.newArrayList(action),
            dayContainer,
            null);



    @Test
    void save_id_not_found_exception() {
        //given
        var day1 = new PlannedDay(day);
        day1.setId(1L);
        given(dayRepository.findById(day1.getId())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, () -> dayService.save(authentication, List.of(day1)));
    }

    @Test
    void save_name_taken_exception() {
        //given
        var savedDay = new PlannedDay(day);
        savedDay.setUserAccount(userAccount);
        savedDay.setId(2L);
        given(dayRepository.findByName(day.getName())).willReturn(Optional.of(savedDay));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> dayService.save(authentication, List.of(day)));
    }

    @Test
    void save_duplicates_found() {
        //given
        var day1 = new PlannedDay(day);
        var day2 = new PlannedDay(day);
        given(dayRepository.findByName(day1.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(DuplicatesFoundException.class, () -> dayService.save(authentication, List.of(day1, day2)));
    }

    @Test
    void save_same_name_different_body_exception(){
        //given
        var day1 = new PlannedDay(day);
        day1.setPlannedActions(null);
        given(dayRepository.findByName(dayContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> dayService.save(authentication ,List.of(day, day1)));
    }

    @Test
    void save_invalid_name_exceptions() {
        //given
        var day1 = new PlannedDay(day);
        day1.setName(null);
        var day2 = new PlannedDay(day);
        day2.setName("11111");
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> dayService.save(authentication, List.of(day1)));
        assertThrows(ValidationExceptionGivenBadName.class, () -> dayService.save(authentication, List.of(day2)));
    }

    @Test
    void save_user_not_confirmed() {
        //given
        var day1 = new PlannedDay(day);
        //then
        assertThrows(UserNotConfirmedException.class, () -> dayService.save(authentication, List.of(day1)));
    }

    @Test
    void save_user_account_not_found() {
        //given
        var day1 = new PlannedDay(day);
        day1.setUserAccount(userAccount);
        given(dayRepository.findByName(day1.getName())).willReturn(Optional.empty());
        //then
        assertThrows(UserAccountNotFoundException.class, () -> dayService.save(authentication, List.of(day1)));
    }

    @Test
    void save_duplicated_actions_in_one_day(){
        //given
        var action1 = new PlannedAction(action);
        var day1 = new PlannedDay(day);
        day1.getPlannedActions().add(action1);
        given(dayRepository.findByName(day1.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(InitializingOfClearRequestObjectException.class, ()->dayService.save(authentication, List.of(day1)));
    }

    @Test
    void sava_actions_with_same_name_different_bodies(){
        //given
        var action1 = new PlannedAction(action);
        action1.setDescription("asdasdasd");
        var day1 = new PlannedDay(day);
        var day2 = new PlannedDay(day1);
        day2.setName("different");
        day2.setPlannedActions(List.of(action1));

        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(InitializingOfClearRequestObjectException.class, ()->dayService.save(authentication, List.of(day1, day2)));
    }

    @Test
    void save_check_if_same_actions_managed_successfully(){
        //given
        var action1 = new PlannedAction(action);
        var action2 = new PlannedAction(action1);

        var day1 = new PlannedDay(day);
        day1.setPlannedActions(Lists.newArrayList(action1));
        var day2 = new PlannedDay(day1);
        day2.setName("different");
        day2.setPlannedActions(Lists.newArrayList(action2));

        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        List<PlannedDay> toSave = Lists.newArrayList(day1, day2);
        given(dayRepository.saveAll(toSave)).willReturn(toSave);

        //when
        dayService.save(authentication, toSave);
        //then
        assertSame(day1.getPlannedActions().get(0), day2.getPlannedActions().get(0));
    }

    @Test
    void save_successfully() {
        //given
        given(dayRepository.findByName(day.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertDoesNotThrow(() -> dayService.save(authentication, List.of(day)));
    }

    @Test
    void get_with_ids() {
        //given
        var day1 = new PlannedDay(day);
        day1.setId(1L);
        var day2 = new PlannedDay(day1);
        day2.setId(2L);
        day2.setName("test2");

        String email = authentication.getName();

        given(dayRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(day1, day2));
        //when
        List<PlannedDay> plannedActions = dayService.get(email, Set.of(1L));
        //then
        assertFalse(plannedActions.contains(day2));
    }

    @Test
    void get_without_ids() {
        //given
        var day1 = new PlannedDay(day);
        day1.setId(1L);
        var day2 = new PlannedDay(day1);
        day2.setId(2L);
        day2.setName("test2");

        String email = authentication.getName();

        given(dayRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(day1, day2));
        //when
        List<PlannedDay> plannedActions = dayService.get(email, null);
        //then
        assertTrue(plannedActions.containsAll(List.of(day1, day2)));
    }

    @Test
    void delete_non_existing_day_exception() {
        //given
        var ids = List.of(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, () -> dayService.delete(ids));
    }

    @Test
    void delete_try_deleting_stuff_that_is_involved_in_another_stuff(){
        //given
        var ids = List.of(2L);
        var day1 = new PlannedDay(day);
        day1.setId(2L);
        var schedule1 = new Schedule();
        schedule1.setId(1L);
        schedule1.setName("some name");
        schedule1.setUserAccount(userAccount);
        schedule1.setDays(Lists.newArrayList(day1));
        var day2 = new PlannedDay(day1);
        day2.setId(3L);
        day2.setName("different");
        var schedule2 = new Schedule();
        schedule2.setId(2L);
        schedule2.setName("some name 2");
        schedule2.setUserAccount(userAccount);
        schedule2.setDays(Lists.newArrayList(day1));

        given(dayRepository.getPlannedStuffHolders(ids.get(0))).willReturn(List.of(schedule1));
        given(dayRepository.findById(2L)).willReturn(Optional.of(day1));
        //when
        var result = dayService.delete(ids);
        //then
        assertTrue(result.get(ids.get(0)).contains(schedule1));
        assertFalse(result.get(ids.get(0)).contains(schedule2));
        verify(dayRepository, times(0)).deleteAllById(any());
    }

    @Test
    void delete_successfully() {
        //given
        var day1 = new PlannedDay(day);
        day1.setId(1L);
        day1.setUserAccount(userAccount);
        var day2 = new PlannedDay(day1);
        day2.setId(2L);
        day2.setName("test2");

        var ids = List.of(1L, 2L);

        given(dayRepository.findById(1L)).willReturn(Optional.of(day1));
        given(dayRepository.findById(2L)).willReturn(Optional.of(day2));

        //when
        dayService.delete(ids);
        //then
        verify(dayRepository, times(1)).deleteAllById(any());
    }

    @Test
    void update_non_existing_day() {
        //given
        var day1 = new PlannedDay(day);
        day1.setUserAccount(userAccount);
        day1.setId(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, () -> dayService.update(authentication, List.of(day1)));
    }

    @Test
    void update_updating_name_already_taken() {
        //given
        var updatingDay = new PlannedDay(day); // 1 already in db
        updatingDay.setId(3L);
        updatingDay.setUserAccount(userAccount);

        var day1 = new PlannedDay(day); // 2 name conflict
        day1.setId(2L);
        day1.setName("updated name");
        day1.setUserAccount(userAccount);

        var day2 = new PlannedDay(day1); // 3 trying to update name in 1
        day2.setId(3L);

        given(dayRepository.findByName(day2.getName())).willReturn(Optional.of(day1));
        given(dayRepository.findById(3L)).willReturn(Optional.of(updatingDay));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> dayService.update(authentication, List.of(day2)));
    }

    @Test
    void update_invalid_name() {
        //given
        var day1 = new PlannedDay(day);
        day1.setId(2L);
        day1.setName("");
        day1.setUserAccount(userAccount);
        var day2 = new PlannedDay(day1);
        day2.setName("11111");
        given(dayRepository.findById(2L)).willReturn(Optional.of(day1));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> dayService.update(authentication, List.of(day1)));
        assertThrows(ValidationExceptionGivenBadName.class, () -> dayService.update(authentication, List.of(day2)));
    }

    @Test
    void update_duplicates_found() {
        //given
        var day1 = new PlannedDay(day);
        day1.setId(2L);
        var day2 = new PlannedDay(day1);
        given(dayRepository.findById(2L)).willReturn(Optional.of(day1));
        //then
        assertThrows(DuplicatesFoundException.class, () -> dayService.update(authentication, List.of(day1, day2)));
    }

    @Test
    void update_same_name_different_body_exception(){
        //given
        var day1 = new PlannedDay(day);
        day1.setId(2L);
        var day2 = new PlannedDay(day1);
        day2.setId(3L);
        day2.setPlannedActions(null);
        given(dayRepository.findById(2L)).willReturn(Optional.of(day1));
        given(dayRepository.findById(3L)).willReturn(Optional.of(day2));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> dayService.update(authentication, List.of(day1, day2)));
    }


    @Test
    void update_successfully(){
        //given
        //configuring mock objects that will serve as day from db
        var previousDayContainer = new PlannedDayContainer(dayContainer);
        previousDayContainer.setId(2L);
        previousDayContainer.setUserAccount(userAccount);
        var actionToBeDeletedFromDayFromDB = new PlannedAction(action);
        actionToBeDeletedFromDayFromDB.setId(1L);
        actionToBeDeletedFromDayFromDB.setName("to be deleted");
        actionToBeDeletedFromDayFromDB.setUserAccount(userAccount);
        var dayFromDB = new PlannedDay(day);
        dayFromDB.setId(1L);
        dayFromDB.setName("to be affected");
        dayFromDB.setPlannedActions(Lists.newArrayList(actionToBeDeletedFromDayFromDB));
        dayFromDB.setPlannedDayContainer(previousDayContainer);
        dayFromDB.setUserAccount(userAccount);

        var day2FromDB = new PlannedDay(dayFromDB);
        day2FromDB.setId(2L);
        day2FromDB.setPlannedActions(Lists.newArrayList());
        day2FromDB.setName("have not to be affected");

        //configuring days from request
        var newDayContainer = new PlannedDayContainer(dayContainer);
        newDayContainer.setBgColor("white");
        var actionToAdd = new PlannedAction(action);
        actionToAdd.setId(3L);
        actionToAdd.setName("to add");
        var actionToBeDeleted = new PlannedAction();
        actionToBeDeleted.setId(-actionToBeDeletedFromDayFromDB.getId());

        var day1 = new PlannedDay(day);
        day1.setId(1L);
        day1.setName("updated name");
        day1.setPlannedDayContainer(newDayContainer);
        day1.setDayOfWeek(DayOfWeek.FRIDAY);
        day.setDate(LocalDate.of(2023, 1,1));
        day1.setPlannedActions(Lists.newArrayList(actionToAdd, actionToBeDeleted));

        var actionNotToBeDeleted = new PlannedAction(actionToBeDeleted);
        actionNotToBeDeleted.setId(actionToBeDeletedFromDayFromDB.getId());
        var day2 = new PlannedDay(day1);
        day2.setId(2L);
        day2.setName("updated name 2");
        day2.setPlannedActions(Lists.newArrayList(actionNotToBeDeleted));

        given(dayRepository.findById(1L)).willReturn(Optional.of(dayFromDB));
        given(dayRepository.getById(1L)).willReturn(dayFromDB);
        given(dayRepository.findById(2L)).willReturn(Optional.of(day2FromDB));
        given(dayRepository.getById(2L)).willReturn(day2FromDB);
        //when
        List<PlannedDay> result = dayService.update(authentication, List.of(day1, day2));
        //then
        assertDoesNotThrow(()->dayService.update(authentication, List.of(day1, day2)));
        assertFalse(result.get(0).getPlannedActions().contains(actionToBeDeleted));
        assertTrue(result.get(1).getPlannedActions().contains(actionNotToBeDeleted));
    }
}