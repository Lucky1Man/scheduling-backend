package com.online.scheduling.schedule.interfaces.initializer;

import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.List;

public interface IPlannedStuffRequirements<T extends IPlannedStuff<T>> {
    List<T> getPlannedStuff();
    UserAccount getUserAccount();
    String getName();
}
