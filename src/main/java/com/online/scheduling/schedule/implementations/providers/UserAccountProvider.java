package com.online.scheduling.schedule.implementations.providers;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.user.entities.User;
import com.online.scheduling.user.implementations.services.UserService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAccountProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext2) throws BeansException {
        applicationContext = applicationContext2;
    }

    public static <T extends IPlannedStuff<T>>
    T insertUserAccount(Authentication authentication, T plannedStuff){
        UserService userService = applicationContext.getBean(UserService.class);
        User user =  userService.getUserByEmail(authentication.getName()).get();
        if(plannedStuff.getUserAccount() == null)
            plannedStuff.setUserAccount(user.getUserAccount());
        return plannedStuff;
    }

    public static <T extends IPlannedStuff<T>> List<T> insertUserAccount(Authentication authentication, List<T> plannedStuffs){
        for(int i = 0; i < plannedStuffs.size(); i++){
            plannedStuffs.get(i).setUserAccount(insertUserAccount(authentication ,plannedStuffs.get(i)).getUserAccount());
        }
        return plannedStuffs;
    }
}
