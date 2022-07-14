package com.online.scheduling.schedule.implementations.providers;

import com.online.scheduling.schedule.implementations.initializers.InitializersHolder;
import com.online.scheduling.schedule.implementations.validators.ValidatorsHolder;
import com.online.scheduling.schedule.repositories.RepositoriesHolder;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SchedulingProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext2) throws BeansException {
        applicationContext = applicationContext2;
    }

    public static ValidatorsHolder getValidator(){
        return applicationContext.getBean(ValidatorsHolder.class);
    }

    public static InitializersHolder getInitializer(){
        return applicationContext.getBean(InitializersHolder.class);
    }

//    public static UserAccountRepository getUserAccountRepository(){return applicationContext.getBean(UserAccountRepository.class);}

    public static RepositoriesHolder getRepositories(){
        return applicationContext.getBean(RepositoriesHolder.class);
    }
}
