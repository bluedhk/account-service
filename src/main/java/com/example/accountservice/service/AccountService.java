package com.example.accountservice.service;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.repository.AccountRepositoryStub;
import com.example.accountservice.repository.BalanceRepositoryStub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepositoryStub accountRepository;
    private final BalanceRepositoryStub balanceRepository;

    public Optional<Balance> getAccountBalance(Long accountId) {
        return accountRepository.findById(accountId).flatMap(balanceRepository::findByAccount);
    }

    @Transactional
    public Account createAccount(String accountNumber) {
        Account account = new Account(null, accountNumber, null, null, null);
        Balance balance = new Balance(null, account, BigDecimal.ZERO);
        account.setBalance(balance);
        return accountRepository.save(account);
    }

    @Transactional
    public Balance deposit(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        Balance balance = balanceRepository.findByAccount(account)
                .orElse(new Balance(null, account, BigDecimal.ZERO));
        balance.setAmount(balance.getAmount().add(amount));
        return balanceRepository.save(balance);
    }

    @Transactional
    public Balance withdraw(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero.");
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        Balance balance = balanceRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Balance not found."));
        if (balance.getAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance.setAmount(balance.getAmount().subtract(amount));
        return balanceRepository.save(balance);
    }
}
