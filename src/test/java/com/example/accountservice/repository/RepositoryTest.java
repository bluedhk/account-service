package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

class RepositoryStubTest {

    private AccountRepositoryStub accountRepositoryStub;
    private BalanceRepositoryStub balanceRepositoryStub;

    @BeforeEach
    void setUp() {
        accountRepositoryStub = new AccountRepositoryStub();
        balanceRepositoryStub = new BalanceRepositoryStub();
    }

    @Test
    void shouldSaveAndRetrieveAccount() {
        // Given
        Account account = new Account(null, "123456", null, null, null);

        // When
        Account savedAccount = accountRepositoryStub.save(account);
        Optional<Account> retrievedAccount = accountRepositoryStub.findById(savedAccount.getId());

        // Then
        then(savedAccount.getId()).isNotNull();
        then(savedAccount.getAccountNumber()).isEqualTo("123456");
        then(retrievedAccount).isPresent();
        then(retrievedAccount.get().getAccountNumber()).isEqualTo("123456");
    }

    @Test
    void shouldSaveAndRetrieveBalance() {
        // Given
        Account account = new Account(1L, "123456", null, null, null);
        Balance balance = new Balance(null, account, BigDecimal.valueOf(1000));
        account.setBalance(balance);

        // When
        Balance savedBalance = balanceRepositoryStub.save(balance);
        Optional<Balance> retrievedBalance = balanceRepositoryStub.findByAccount(account);

        // Then
        then(savedBalance.getId()).isNotNull();
        then(savedBalance.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
        then(retrievedBalance).isPresent();
        then(retrievedBalance.get().getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }
}