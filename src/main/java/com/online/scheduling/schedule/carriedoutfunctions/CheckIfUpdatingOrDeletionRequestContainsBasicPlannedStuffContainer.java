package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.enums.RequestTypes;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckIfUpdatingOrDeletionRequestContainsBasicPlannedStuffContainer {
    public static final String defaultNameOfPlannedStuff = "planned stuff container";
    public static void checkIfContainsBasicPlannedStuffContainer(List<Long> ids, RequestTypes typeOfRequest, String nameOfPlannedStuff){
        ids = Set.copyOf(ids).stream().toList();
        if(ids.contains(1L))
            throw new UnableToExecuteQueryException(String.format("You can not %s basic %s container", typeOfRequest.name().toLowerCase(), nameOfPlannedStuff));
    }

    public static void checkIfContainsBasicPlannedStuffContainer(List<Long> ids, RequestTypes typeOfRequest){
        checkIfContainsBasicPlannedStuffContainer(ids, typeOfRequest, defaultNameOfPlannedStuff);
    }

    public static <T extends IPlannedStuff<T>>
    void checkIfContainsBasicPlannedStuffContainer(RequestTypes typeOfRequest, List<T> listOfPlannedStuff, String nameOfPlannedStuff){
        List<Long> listOfIds = new ArrayList<>();
        for(var elem : listOfPlannedStuff){
            Long id = elem.getId();
            if(!(id == 1L && elem.isBlank()))
                listOfIds.add(id);
        }
        checkIfContainsBasicPlannedStuffContainer(
                listOfIds,
                typeOfRequest,
                nameOfPlannedStuff);
    }

    public static <T extends IPlannedStuff<T>>
    void checkIfContainsBasicPlannedStuffContainer(RequestTypes typeOfRequest, List<T> listOfPlannedStuff){
        checkIfContainsBasicPlannedStuffContainer(typeOfRequest, listOfPlannedStuff, defaultNameOfPlannedStuff);
    }
}
