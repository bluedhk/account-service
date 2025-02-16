package com.example.accountservice.controller.web;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AccountUIController {
    private final AccountService accountService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/account/{id}")
    public String getAccountBalance(@PathVariable Long id, Model model) {
        Optional<Balance> balance = accountService.getAccountBalance(id);
        model.addAttribute("accountId", id);
        model.addAttribute("balance", balance.map(Balance::getAmount).orElse(BigDecimal.ZERO));
        return "account";
    }

    @PostMapping("/account/create")
    public String createAccount(@RequestParam String accountNumber, Model model) {
        Account newAccount = accountService.createAccount(accountNumber);
        model.addAttribute("accountId", newAccount.getId());
        model.addAttribute("balance", newAccount.getBalance().getAmount());
        return "account";
    }

    @PostMapping("/account/{id}/deposit")
    public String deposit(@PathVariable Long id, @RequestParam BigDecimal amount, Model model) {
        Balance updatedBalance = accountService.deposit(id, amount);
        model.addAttribute("accountId", id);
        model.addAttribute("balance", updatedBalance.getAmount());
        return "account";
    }

    @PostMapping("/account/{id}/withdraw")
    public String withdraw(@PathVariable Long id, @RequestParam BigDecimal amount, Model model) {
        Balance updatedBalance = accountService.withdraw(id, amount);
        model.addAttribute("accountId", id);
        model.addAttribute("balance", updatedBalance.getAmount());
        return "account";
    }
}