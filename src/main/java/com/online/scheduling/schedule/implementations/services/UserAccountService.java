package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.schedule.repositories.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public List<UserAccount> getUserAccounts(List<Long> ids) {
        if(ids == null || ids.isEmpty())
            return userAccountRepository.findAll();
        return userAccountRepository.findAllById(ids);
    }

    public void delete(List<Long> ids) {

    }
    public void save(UserAccount userAccount){
        userAccountRepository.save(userAccount);
    }
}
