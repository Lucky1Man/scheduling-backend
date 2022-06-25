package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.List;

public class RemoveMarkedPlannedStuffFromItsOwner {
    public static <COM extends IPlannedStuff<COM>, TO_MANAGE extends IPlannedStuffRequirements<COM>>
    List<TO_MANAGE> removeMarkedPlannedStuffFromItsOwner(List<TO_MANAGE> toManages, List<List<COM>> idsOfPlannedStuffToBeRemovedFromItsRequired){
        int i = 0;
        for(var stuff : toManages){
            var plannedActionsToRemove = idsOfPlannedStuffToBeRemovedFromItsRequired.get(i);
            for(int j = 0; j < stuff.getPlannedStuff().size(); j++){
                var oneStuff = stuff.getPlannedStuff().get(j);
                for(var actionToRem : plannedActionsToRemove){
                    if(oneStuff.getId().equals(actionToRem.getId()))
                        stuff.getPlannedStuff().remove(j);
                }
            }
            i++;
        }
        return toManages;
    }
}
