package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.List;

public class RemoveMarkedInnerStuffFromMainStuff {

    public static
    <INNER extends IPlannedStuff<INNER>, MAIN extends IPlannedStuffRequirements<INNER>>
    void removeMarkedInnerStuffFromMainStuff(MAIN stuffFromDB, List<INNER> listThatContainsMarkedInnerStuff){
        List<INNER> innerStuffsFromStuffFromDB = stuffFromDB.getPlannedStuff();
        for(int i = 0; i < innerStuffsFromStuffFromDB.size(); i++){
            INNER innerStuffFromStuffFromDB = innerStuffsFromStuffFromDB.get(i);
            for(var innerStuffFromRequest : listThatContainsMarkedInnerStuff){
                if(innerStuffFromStuffFromDB.getId().equals(-innerStuffFromRequest.getId())){
                    innerStuffsFromStuffFromDB.remove(i);
                    i--;
                }
            }
        }
    }
}
