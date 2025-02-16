package com.example.accountservice.repository;

import com.example.accountservice.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryCustom {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAllAccounts();
}