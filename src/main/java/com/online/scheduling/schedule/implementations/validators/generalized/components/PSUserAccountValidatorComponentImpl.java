package com.online.scheduling.schedule.implementations.validators.generalized.components;

import com.online.scheduling.exceptions.UserAccountNotAttachedException;
import com.online.scheduling.exceptions.UserAccountNotFoundException;
import com.online.scheduling.schedule.entities.UserAccount;
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
        UserAccount userAccount = validated.getUserAccount();
        if(userAccount == null)
            throw new UserAccountNotAttachedException("%s {%s} does not have user account".formatted(prefix, validated));

        userAccountRepository.findById(userAccount.getId())
                .orElseThrow(()->new UserAccountNotFoundException(String.format("User account %s not found", userAccount)));
        return true;
    }
}
