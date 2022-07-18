package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.ArrayList;
import java.util.List;

public class GetInnerStuffToBeAddedToMainStuff {

    public static < INNER extends IPlannedStuff<INNER>, MAIN extends IPlannedStuffRequirements<INNER>>
    List<INNER> getStuffToBeAdded(MAIN stuffFromDB, List<INNER> innerStuffsFromRequest){
        List<INNER> result = new ArrayList<>();
        for(var innerStuffFromRequest : innerStuffsFromRequest){
            boolean wasFound = false;
            Long innerStuffId = innerStuffFromRequest.getId();

            for(var innerStuffFromDB : stuffFromDB.getPlannedStuff()){
                if(innerStuffId != null && innerStuffId.equals(innerStuffFromDB.getId())){
                    wasFound = true;
                    break;
                }
            }
            if(!wasFound && innerStuffId != null && innerStuffId > 0)
                result.add(innerStuffFromRequest);
        }
        return result;
    }
}
