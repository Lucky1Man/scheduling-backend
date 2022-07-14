package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

import java.util.ArrayList;
import java.util.List;

public class GetStuffIfAllValid {

    public static <T extends IPlannedStuff<T>> List<T> getStuffIfAllValid(List<T> validated, IPlannedStuffValidator<T> stuffValidator){
        List<T> result = new ArrayList<>();
        if(stuffValidator.validateAll(validated)){
            result.addAll(validated);
            return result;
        }
        return result;
    }
}
