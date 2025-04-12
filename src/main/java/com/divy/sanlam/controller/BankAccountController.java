package com.divy.sanlam.controller;

import com.divy.sanlam.service.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.divy.sanlam.model.WithdrawalRecord;


import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long accountId,
                                           @RequestParam BigDecimal amount) {
        bankAccountService.withdraw(accountId, amount);
        return ResponseEntity.ok("Withdrawal successful");
    }
    @GetMapping("/history")
    public ResponseEntity<List<WithdrawalRecord>> getWithdrawalHistory(@RequestParam Long accountId) {
        List<WithdrawalRecord> history = bankAccountService.getWithdrawalHistory(accountId);
        return ResponseEntity.ok(history);
    }   

}
