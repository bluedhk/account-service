package com.example.accountservice.controller;

import com.example.accountservice.service.AccountService;
import com.example.accountservice.service.vo.AccountDto;
import com.example.accountservice.service.vo.BalanceDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create account", description = "Create a new account with an initial balance of 0")
    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.createAccount(accountNumber));
    }

    @Operation(summary = "Get account balance", description = "Retrieve the balance of a specific account by ID")
    @GetMapping("/{id}/balance")
    public ResponseEntity<AccountDto> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountBalance(id));
    }

    @Operation(summary = "Deposit money", description = "Deposit a specific amount into an account")
    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody BalanceDto balanceDto) {
        return ResponseEntity.ok(accountService.deposit(id, balanceDto));
    }

    @Operation(summary = "Withdraw money", description = "Withdraw a specific amount from an account")
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @RequestBody BalanceDto balanceDto) {
        return ResponseEntity.ok(accountService.withdraw(id, balanceDto));
    }
}