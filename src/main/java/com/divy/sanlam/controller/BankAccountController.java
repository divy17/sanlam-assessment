package com.divy.sanlam.controller;

import com.divy.sanlam.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        return bankAccountService.withdraw(accountId, amount);
    }
}
