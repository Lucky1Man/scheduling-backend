package com.online.scheduling.schedule.interfaces;

import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;

import java.util.List;

public interface IInnerPlannedStuffRepository
        <INNER_T extends IPlannedStuff<INNER_T>, HOLDER_T extends IPlannedStuff<HOLDER_T>>
        extends IPlannedStuffRepository<INNER_T> {
    List<HOLDER_T> getPlannedStuffHolders(Long idOfContainer);
}
