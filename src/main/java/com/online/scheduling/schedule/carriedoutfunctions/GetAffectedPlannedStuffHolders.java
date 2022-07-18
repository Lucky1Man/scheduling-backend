package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IInnerPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAffectedPlannedStuffHolders {
    public static <INNER_T extends IPlannedStuff<INNER_T>, HOLDER_T extends IPlannedStuff<HOLDER_T>>
    Map<Long ,List<HOLDER_T>> getAffectedPlannedStuffHolders(
            List<Long> innerStuffsId,
            IInnerPlannedStuffRepository<INNER_T, HOLDER_T> innerStuffRepository){
        var result = new HashMap<Long, List<HOLDER_T>>();
        for(var stuffId : innerStuffsId){
            List<HOLDER_T> affectedStuffHolders = innerStuffRepository.getPlannedStuffHolders(stuffId);
            if(affectedStuffHolders != null && !affectedStuffHolders.isEmpty()){
                result.put(stuffId, affectedStuffHolders);
            }else {
                result.put(stuffId, new ArrayList<>());
            }
        }
        return result;
    }
}
