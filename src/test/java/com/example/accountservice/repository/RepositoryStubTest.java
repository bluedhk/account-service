package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.service.enums.BalanceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        LocalDateTime now = LocalDateTime.now();
        Account account = Account.ofAll(null, "123456", new ArrayList<>(), now, now);

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
        Account account = Account.ofAll(1L, "123456", new ArrayList<>(), null, null);
        Balance balance = Balance.ofAll(null, BalanceType.STANDARD.name(), account, BigDecimal.valueOf(1000));

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