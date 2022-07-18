package com.online.scheduling.schedule.interfaces;

import com.online.scheduling.schedule.entities.UserAccount;

public interface IPlannedStuff<T> {
    Long getId();

    String getName();

    UserAccount getUserAccount();

    void setUserAccount(UserAccount userAccount);

    boolean equals(Object o);

    T copyProperties(T from);

    //needs to be updated if new fields are added or old deleted
    //used to avoid calling update method in service class if just adding this object to another object
    default boolean isBlank() {
        if (this.getId() == null)
            throw new IllegalStateException(
                    "Illegal usage of method isBlank() on PlannedStuff: %s".formatted(this));
        return this.getName() == null && this.getUserAccount() == null;
    }
}
