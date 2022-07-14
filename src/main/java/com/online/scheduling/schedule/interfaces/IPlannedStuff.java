package com.online.scheduling.schedule.interfaces;

import com.online.scheduling.schedule.entities.UserAccount;

public interface IPlannedStuff<T>{
    Long getId();
    String getName();
    UserAccount getUserAccount();
    void setUserAccount(UserAccount userAccount);
    boolean equals(Object o);

    T copyProperties(T from);
}
