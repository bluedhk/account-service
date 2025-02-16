package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepositoryStub {
    private final ConcurrentHashMap<Long, Account> accountStore = new ConcurrentHashMap<>();
    
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountStore.get(id));
    }
    
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountStore.values().stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst();
    }
    
    public Account save(Account account) {
        if (account.getId() == null) {
            account.setId((long) (accountStore.size() + 1));
        }
        accountStore.put(account.getId(), account);
        return account;
    }
}
