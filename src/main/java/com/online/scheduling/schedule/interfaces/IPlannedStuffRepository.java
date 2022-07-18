package com.online.scheduling.schedule.interfaces;

import com.online.scheduling.schedule.entities.PlannedAction;

import java.util.List;
import java.util.Optional;

public interface IPlannedStuffRepository<T extends IPlannedStuff<T>>{
    Optional<T> findByName(String name);
    List<T> findAllByUserAccount_Owner_Email(String email);
    Optional<T> findByUserAccount_Owner_EmailAndName(String email, String name);
}
