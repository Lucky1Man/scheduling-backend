package com.online.scheduling.schedule.controllers;

import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.schedule.implementations.services.UserAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-account")
public class UserAccountController {
    private final UserAccountService accountService;

    public UserAccountController(UserAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<UserAccount> get(@RequestParam(name = "ids",required = false) List<Long> ids){
        return accountService.getUserAccounts(ids);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "ids",required = false) List<Long> ids){
        accountService.delete(ids);
    }

}
