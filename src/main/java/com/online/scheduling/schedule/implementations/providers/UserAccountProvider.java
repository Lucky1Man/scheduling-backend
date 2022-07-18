package com.online.scheduling.schedule.implementations.providers;

import com.online.scheduling.exceptions.UserNotConfirmedException;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class UserAccountProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext2) throws BeansException {
        applicationContext = applicationContext2;
    }

    public static <T extends IPlannedStuff<T>>
    void insertUserAccountIfNeeded(Authentication authentication, T plannedStuff){
        if(plannedStuff.getUserAccount() != null)
            return;
        var userAccountRepository = applicationContext.getBean(UserAccountRepository.class);
        String email = authentication.getName();
        var account = userAccountRepository.findByOwner_Email(email)
                .orElseThrow(()->new UserNotConfirmedException(String.format("Email %s not confirmed",email)));
        plannedStuff.setUserAccount(account);
    }

    public static <T extends IPlannedStuff<T>> void insertUserAccountIfNeeded(Authentication authentication, List<T> plannedStuffs){
        if( plannedStuffs == null || plannedStuffs.isEmpty() || plannedStuffs.stream().allMatch(Objects::isNull))
            return;
        for (T plannedStuff : plannedStuffs) {
            insertUserAccountIfNeeded(authentication, plannedStuff);
        }
    }
}
