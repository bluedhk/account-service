package com.example.accountservice.controller;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @Operation(summary = "Create account", description = "Create a new account with an initial balance of 0")
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.createAccount(accountNumber));
    }

    @Operation(summary = "Get account balance", description = "Retrieve the balance of a specific account by ID")
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        Optional<Balance> balance = accountService.getAccountBalance(id);
        return balance.map(value -> ResponseEntity.ok(value.getAmount()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deposit money", description = "Deposit a specific amount into an account")
    @PostMapping("/{id}/deposit")
    public ResponseEntity<Balance> deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(accountService.deposit(id, amount));
    }

    @Operation(summary = "Withdraw money", description = "Withdraw a specific amount from an account")
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Balance> withdraw(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(accountService.withdraw(id, amount));
    }
}