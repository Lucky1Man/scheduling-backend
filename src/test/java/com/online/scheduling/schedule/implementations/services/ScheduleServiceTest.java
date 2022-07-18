package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.*;
import com.online.scheduling.schedule.entities.*;
import com.online.scheduling.schedule.repositories.ScheduleRepository;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import com.online.scheduling.user.entities.User;
import com.online.scheduling.user.implementations.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.profiles.active=test")
class ScheduleServiceTest {
    @Autowired
    ScheduleService scheduleService;

    @MockBean
    ScheduleRepository scheduleRepository;
    @MockBean
    UserAccountRepository userAccountRepository;
    @MockBean
    ScheduleContainerService scheduleContainerService;
    @MockBean
    PlannedDayService dayService;
    @MockBean
    UserService userService;

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
    PlannedDayContainer dayContainer = new PlannedDayContainer(null, "test day container", "white",null);
    PlannedDay day = new PlannedDay(
            null,
            LocalDate.of(2022, 1,1),
            DayOfWeek.TUESDAY,
            "test day name",
            Lists.newArrayList(),
            dayContainer,
            null);

    ScheduleContainer scheduleContainer = new ScheduleContainer(null, "test schedule container", "white", null);
    Schedule schedule = new Schedule(
            null,
            "test schedule name",
            user,
            null,
            Lists.newArrayList(day),
            scheduleContainer,
            null);



    @Test
    void save_id_not_found_exception() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setId(1L);
        given(scheduleRepository.findById(schedule1.getId())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, () -> scheduleService.save(authentication, List.of(schedule1)));
    }

    @Test
    void save_name_taken_exception() {
        //given
        var savedSchedule = new Schedule(schedule);
        savedSchedule.setId(2L);
        savedSchedule.setUserAccount(userAccount);
        given(scheduleRepository.findByName(schedule.getName())).willReturn(Optional.of(savedSchedule));
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> scheduleService.save(authentication, List.of(schedule)));
    }

    @Test
    void save_duplicates_found() {
        //given
        var schedule1 = new Schedule(schedule);
        var schedule2 = new Schedule(schedule);
        given(scheduleRepository.findByName(schedule1.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(DuplicatesFoundException.class, () -> scheduleService.save(authentication, List.of(schedule1, schedule2)));
    }

    @Test
    void save_same_name_different_body_exception(){
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setDays(null);
        given(scheduleRepository.findByName(scheduleContainer.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> scheduleService.save(authentication ,List.of(schedule, schedule1)));
    }

    @Test
    void save_invalid_name_exceptions() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setName(null);
        var schedule2 = new Schedule(schedule);
        schedule2.setName("11111");
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> scheduleService.save(authentication, List.of(schedule1)));
        assertThrows(ValidationExceptionGivenBadName.class, () -> scheduleService.save(authentication, List.of(schedule2)));
    }

    @Test
    void save_user_not_confirmed() {
        //given
        var schedule1 = new Schedule(schedule);
        //then
        assertThrows(UserNotConfirmedException.class, () -> scheduleService.save(authentication, List.of(schedule1)));
    }

    @Test
    void save_user_account_not_found() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setUserAccount(userAccount);
        given(scheduleRepository.findByName(schedule1.getName())).willReturn(Optional.empty());
        //then
        assertThrows(UserAccountNotFoundException.class, () -> scheduleService.save(authentication, List.of(schedule1)));
    }

    @Test
    void save_duplicated_days_in_one_schedule(){
        //given
        var day1 = new PlannedDay(day);
        var schedule1 = new Schedule(schedule);
        schedule1.getDays().add(day1);
        given(scheduleRepository.findByName(schedule1.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        given(userService.getUserByEmail(user.getEmail())).willReturn(Optional.of(user));
        //then
        assertThrows(InitializingOfClearRequestObjectException.class, ()-> scheduleService.save(authentication, List.of(schedule1)));
    }

    @Test
    void save_schedules_with_same_name_different_bodies(){
        //given
        var day1 = new PlannedDay(day);
        day1.setPlannedActions(null);
        var schedule1 = new Schedule(schedule);
        var schedule2 = new Schedule(schedule1);
        schedule2.setName("different");
        schedule2.setDays(List.of(day1));

        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        given(userService.getUserByEmail(user.getEmail())).willReturn(Optional.of(user));
        //then
        assertThrows(InitializingOfClearRequestObjectException.class, ()-> scheduleService.save(authentication, List.of(schedule1, schedule2)));
    }

    @Test
    void save_check_if_same_days_managed_successfully(){
        //given
        var day1 = new PlannedDay(day);
        var day2 = new PlannedDay(day1);

        var schedule1 = new Schedule(schedule);
        schedule1.setDays(Lists.newArrayList(day1));
        var schedule2 = new Schedule(schedule1);
        schedule2.setName("different");
        schedule2.setDays(Lists.newArrayList(day2));

        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        List<Schedule> toSave = Lists.newArrayList(schedule1, schedule2);
        given(scheduleRepository.saveAll(toSave)).willReturn(toSave);
        given(userService.getUserByEmail(user.getEmail())).willReturn(Optional.of(user));

        //when
        scheduleService.save(authentication, toSave);
        //then
        assertSame(schedule1.getDays().get(0), schedule2.getDays().get(0));
    }

    @Test
    void save_successfully() {
        //given
        given(scheduleRepository.findByName(schedule.getName())).willReturn(Optional.empty());
        given(userAccountRepository.findByOwner_Email(authentication.getName())).willReturn(Optional.of(userAccount));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        given(userService.getUserByEmail(user.getEmail())).willReturn(Optional.of(user));
        //then
        assertDoesNotThrow(() -> scheduleService.save(authentication, List.of(schedule)));
    }

    @Test
    void get_with_ids() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setId(1L);
        var schedule2 = new Schedule(schedule1);
        schedule2.setId(2L);
        schedule2.setName("test2");

        String email = authentication.getName();

        given(scheduleRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(schedule1, schedule2));
        //when
        List<Schedule> plannedActions = scheduleService.get(email, Set.of(1L));
        //then
        assertFalse(plannedActions.contains(schedule2));
    }

    @Test
    void get_without_ids() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setId(1L);
        var schedule2 = new Schedule(schedule1);
        schedule2.setId(2L);
        schedule2.setName("test2");

        String email = authentication.getName();

        given(scheduleRepository.findAllByUserAccount_Owner_Email(email)).willReturn(List.of(schedule1, schedule2));
        //when
        List<Schedule> plannedActions = scheduleService.get(email, null);
        //then
        assertTrue(plannedActions.containsAll(List.of(schedule1, schedule2)));
    }

    @Test
    void delete_non_existing_schedule_exception() {
        //given
        var ids = List.of(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, () -> scheduleService.delete(ids));
    }

    @Test
    void delete_successfully() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setId(1L);
        schedule1.setUserAccount(userAccount);
        var schedule2 = new Schedule(schedule1);
        schedule2.setId(2L);
        schedule2.setName("test2");

        var ids = List.of(1L, 2L);

        given(scheduleRepository.findById(1L)).willReturn(Optional.of(schedule1));
        given(scheduleRepository.findById(2L)).willReturn(Optional.of(schedule2));

        //when
        scheduleService.delete(ids);
        //then
        verify(scheduleRepository, times(1)).deleteAllById(any());
    }

    @Test
    void update_non_existing_schedule() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setUserAccount(userAccount);
        schedule1.setId(2L);
        //then
        assertThrows(ValidationExceptionGivenNonExistingId.class, () -> scheduleService.update(authentication, List.of(schedule1)));
    }

    @Test
    void update_updating_name_already_taken() {
        //given
        var updatingSchedule = new Schedule(schedule); // 1 already in db
        updatingSchedule.setId(3L);
        updatingSchedule.setUserAccount(userAccount);

        var schedule1 = new Schedule(schedule); // 2 name conflict
        schedule1.setId(2L);
        schedule1.setName("updated name");
        schedule1.setUserAccount(userAccount);

        var schedule2 = new Schedule(schedule1); // 3 trying to update name in 1
        schedule2.setId(3L);

        given(scheduleRepository.findByName(schedule2.getName())).willReturn(Optional.of(schedule1));
        given(scheduleRepository.findById(3L)).willReturn(Optional.of(updatingSchedule));
        given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.of(userAccount));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> scheduleService.update(authentication, List.of(schedule2)));
    }

    @Test
    void update_invalid_name() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setId(2L);
        schedule1.setName("");
        schedule1.setUserAccount(userAccount);
        var schedule2 = new Schedule(schedule1);
        schedule2.setName("11111");
        given(scheduleRepository.findById(2L)).willReturn(Optional.of(schedule1));
        //then
        assertThrows(ValidationExceptionGivenBadName.class, () -> scheduleService.update(authentication, List.of(schedule1)));
        assertThrows(ValidationExceptionGivenBadName.class, () -> scheduleService.update(authentication, List.of(schedule2)));
    }

    @Test
    void update_duplicates_found() {
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setId(2L);
        var schedule2 = new Schedule(schedule1);
        given(scheduleRepository.findById(2L)).willReturn(Optional.of(schedule1));
        //then
        assertThrows(DuplicatesFoundException.class, () -> scheduleService.update(authentication, List.of(schedule1, schedule2)));
    }

    @Test
    void update_same_name_different_body_exception(){
        //given
        var schedule1 = new Schedule(schedule);
        schedule1.setId(2L);
        var schedule2 = new Schedule(schedule1);
        schedule2.setId(3L);
        schedule2.setDays(null);
        given(scheduleRepository.findById(2L)).willReturn(Optional.of(schedule1));
        given(scheduleRepository.findById(3L)).willReturn(Optional.of(schedule2));
        //then
        assertThrows(PlannedStuffConflictException.class, ()-> scheduleService.update(authentication, List.of(schedule1, schedule2)));
    }


    @Test
    void update_successfully(){
        //given
        //configuring mock objects that will serve as day from db
        var previousScheduleContainer = new ScheduleContainer(scheduleContainer);
        previousScheduleContainer.setId(2L);
        previousScheduleContainer.setUserAccount(userAccount);
        var dayToBeDeletedFromScheduleFromDB = new PlannedDay(day);
        dayToBeDeletedFromScheduleFromDB.setId(1L);
        dayToBeDeletedFromScheduleFromDB.setName("to be deleted");
        dayToBeDeletedFromScheduleFromDB.setUserAccount(userAccount);
        var scheduleFromDB = new Schedule(schedule);
        scheduleFromDB.setId(1L);
        scheduleFromDB.setName("to be affected");
        scheduleFromDB.setDays(Lists.newArrayList(dayToBeDeletedFromScheduleFromDB));
        scheduleFromDB.setScheduleContainer(previousScheduleContainer);
        scheduleFromDB.setUserAccount(userAccount);

        var schedule2FromDB = new Schedule(scheduleFromDB);
        schedule2FromDB.setId(2L);
        schedule2FromDB.setDays(Lists.newArrayList());
        schedule2FromDB.setName("have not to be affected");

        //configuring schedules from request
        var newScheduleContainer = new ScheduleContainer(scheduleContainer);
        newScheduleContainer.setBgColor("white");
        var dayToAdd = new PlannedDay(day);
        dayToAdd.setId(3L);
        dayToAdd.setName("to add");
        var dayToBeDeleted = new PlannedDay();
        dayToBeDeleted.setId(-dayToBeDeletedFromScheduleFromDB.getId());

        var schedule1 = new Schedule(schedule);
        schedule1.setId(1L);
        schedule1.setName("updated name");
        schedule1.setScheduleContainer(newScheduleContainer);
        schedule1.setDays(Lists.newArrayList(dayToAdd, dayToBeDeleted));

        var dayNotToBeDeleted = new PlannedDay(dayToBeDeleted);
        dayNotToBeDeleted.setId(dayToBeDeletedFromScheduleFromDB.getId());
        var schedule2 = new Schedule(schedule1);
        schedule2.setId(2L);
        schedule2.setName("updated name 2");
        schedule2.setDays(Lists.newArrayList(dayNotToBeDeleted));

        given(scheduleRepository.findById(1L)).willReturn(Optional.of(scheduleFromDB));
        given(scheduleRepository.getById(1L)).willReturn(scheduleFromDB);
        given(scheduleRepository.findById(2L)).willReturn(Optional.of(schedule2FromDB));
        given(scheduleRepository.getById(2L)).willReturn(schedule2FromDB);
        //when
        List<Schedule> result = scheduleService.update(authentication, List.of(schedule1, schedule2));
        //then
        assertDoesNotThrow(()-> scheduleService.update(authentication, List.of(schedule1, schedule2)));
        assertFalse(result.get(0).getDays().contains(dayToBeDeleted));
        assertTrue(result.get(1).getDays().contains(dayNotToBeDeleted));
    }
}