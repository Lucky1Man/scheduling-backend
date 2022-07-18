package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.exceptions.DeletionRequestHolderStuffAffectedException;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckIfContainsPlannedStuffHolders {
    public static <HOLDER_T extends IPlannedStuff<HOLDER_T>>
    boolean checkIfContains(List<Long> innerStuffIds, Map<Long, List<HOLDER_T>> resultFromGetAffectedFunc){
        for(var id : innerStuffIds){
            List<HOLDER_T> holders = resultFromGetAffectedFunc.get(id);
            if((holders != null && !holders.isEmpty()))
                return true;
        }
        return false;
    }
}
