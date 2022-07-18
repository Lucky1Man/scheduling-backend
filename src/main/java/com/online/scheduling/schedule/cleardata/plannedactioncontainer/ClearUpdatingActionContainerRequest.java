package com.online.scheduling.schedule.cleardata.plannedactioncontainer;

import com.online.scheduling.schedule.cleardata.generalized.ClearUpdatingPlannedStuffContainerRequestTemplate;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

import java.util.List;

public class ClearUpdatingActionContainerRequest extends ClearUpdatingPlannedStuffContainerRequestTemplate<PlannedActionContainer> {
    public ClearUpdatingActionContainerRequest(List<PlannedActionContainer> plannedActionContainers) {
        super(plannedActionContainers,"action container");
    }

    @Override
    protected IPlannedStuffValidator<PlannedActionContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getActionContainerValidator();
    }
//    @Getter
//    private final List<PlannedActionContainer> actionContainers;
//    private final PlannedActionContainerValidator actionContainerValidator;
//
//    public ClearUpdatingActionContainerRequest(
//            List<PlannedActionContainer> actionContainers) {
//        this.actionContainers = actionContainers;
//        this.actionContainerValidator = SchedulingProvider.getValidator().getActionContainerValidator();
//        init();
//    }
//    private void init(){
//        validateAllIgnoringNulls();
//    }
//
//    private void validateAllIgnoringNulls(){
//        for(var container : actionContainers){
//            if(container.getId() == null)
//                throw new UnableToExecuteQueryException(String.format("Updating action container {%s} can not have empty id",container));
//            actionContainerValidator.validateUsing(container, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
//            if(container.getName() != null)
//                actionContainerValidator.validateUsing(container,  List.of(PS_IF_NAME_TAKEN_VALIDATOR_COMPONENT.getId(), PS_NAME_VALIDATOR_COMPONENT.getId()));
//        }
//    }
}
