package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.ArrayList;
import java.util.List;

public class CheckIfUpdatingStuffNeedsToBeAddedToItsOwner {
    public static <T extends IPlannedStuff<T>, OWN extends IPlannedStuffRequirements<T>>
    List<T> getUpdatingPlannedStuffToBeAdded(
            List<T> stuffToUpdate,
            List<OWN> stuffOwners,
            IPlannedStuffRepository<OWN> ownerRepository){
        var result = new ArrayList<T>();
        for(var owner : stuffOwners){
            var currentOwner = ownerRepository.findByUserAccount_Owner_EmailAndName(owner.getUserAccount().getOwner().getEmail(), owner.getName()).get();
            List<T> plannedStuff = currentOwner.getPlannedStuff();
            if(plannedStuff == null || plannedStuff.isEmpty())
                result.addAll(stuffToUpdate);
            for(var currentStuff : plannedStuff){
                boolean wasFound = false;
                for(var searchingStuff : stuffToUpdate){
                    if(currentStuff.getName().equals(searchingStuff.getName())){
                        wasFound = true;
                        break;
                    }
                }
                if(!wasFound)
                    result.add(currentStuff);
            }

        }
        return result;
    }
}
