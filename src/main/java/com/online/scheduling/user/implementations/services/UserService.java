package com.online.scheduling.user.implementations.services;

import com.online.scheduling.exceptions.UserNotFoundException;
import com.online.scheduling.user.config.UserConfig;
import com.online.scheduling.user.entities.User;
import com.online.scheduling.user.repositories.UserRepository;
import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;
import com.online.scheduling.user.interfaces.IUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service("UserService")
public class UserService {
    private final UserRepository repository;
    private final IUserValidator userValidator;
    private final UserConfig validatorConfig;

    @Autowired
    public UserService(UserRepository repository,
                       @Qualifier("UserValidator")
                           IUserValidator userValidator,
                       @Qualifier("UserConfig")
                       UserConfig validatorConfig) {
        this.repository = repository;
        this.userValidator = userValidator;
        this.validatorConfig = validatorConfig;
    }

    public void saveUser(User user) throws EmailAlreadyRegisteredException {
        if(userValidator.validateAll(user,validatorConfig.getValidators()))
            repository.save(user);
    }
    public Optional<User> getUserByEmail(String email){
        return repository.findByEmail(email);
    }

    public void enableAppUser(String email) {
        repository.enableAppUser(email);
    }
    @Transactional
    public void setUserAccount(User user){
        Long id = user.getId();
        User byId = repository.findById(id).orElseThrow(()->new UserNotFoundException(String.format("User with id %s not found",id)));
        byId.setUserAccount(user.getUserAccount());
    }
}
