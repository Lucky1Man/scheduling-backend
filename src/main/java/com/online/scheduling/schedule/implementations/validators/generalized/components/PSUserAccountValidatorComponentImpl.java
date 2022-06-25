package com.online.scheduling.schedule.implementations.validators.generalized.components;

import com.online.scheduling.exceptions.ValidationExceptionUserAccountException;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import lombok.Getter;
import lombok.Setter;

public class PSUserAccountValidatorComponentImpl<T extends IPlannedStuff<T>> implements IPlannedStuffValidatorComponent<T> {
    private final UserAccountRepository userAccountRepository;
    @Getter
    @Setter
    private String prefix;

    public PSUserAccountValidatorComponentImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public boolean isValid(T validated) {
//        var userAccount = validated.getUserAccount();
//        if(userAccount == null)
//            throw new ValidationExceptionUserAccountException(String.format("%s does not have user account info {%s}",prefix, validated)); //TODO provide exceptions
//        Long id = userAccount.getId();
//        if(id != null){
//            userAccountRepository.findById(id)
//                    .orElseThrow(()->new ValidationExceptionUserAccountException(
//                            String.format("Trying to save %s with non-existent user account %s",prefix,userAccount)));
//        }
        return true;
    }
}
