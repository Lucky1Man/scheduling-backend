package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

import java.util.ArrayList;
import java.util.List;

public class GetStuffIfAllValid {
    private static final String defaultName = "planned stuff";

    public static <T extends IPlannedStuff<T>>
    List<T> getStuffIfAllValid(List<T> validated, IPlannedStuffValidator<T> stuffValidator, String plannedStuffName){
        List<T> result = new ArrayList<>();
        if(stuffValidator.validateAll(validated)){
            for(var val : validated){
                if(val.getId() != null)
                    throw new UnableToExecuteQueryException("You can not specify %s id when trying to save it".formatted(plannedStuffName));
            }
            result.addAll(validated);
            return result;
        }
        return result;
    }

    public static <T extends IPlannedStuff<T>>
    List<T> getStuffIfAllValid(List<T> validated, IPlannedStuffValidator<T> stuffValidator){
        return getStuffIfAllValid(validated, stuffValidator, defaultName);
    }
}
