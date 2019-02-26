package com.kubernetes.konekt.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.service.AccountService;

@RestController
@RequestMapping("/api")
public class UserRestController {
    @Autowired
    AccountService accountService;
    
    @GetMapping("/accounts")
    public List<Account> getAll(){
        return accountService.getAccounts();
    }
}
