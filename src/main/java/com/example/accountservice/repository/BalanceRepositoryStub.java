package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BalanceRepositoryStub {
    private final ConcurrentHashMap<Long, Balance> balanceStore = new ConcurrentHashMap<>();
    
    public Optional<Balance> findByAccount(Account account) {
        return balanceStore.values().stream()
                .filter(balance -> balance.getAccount().equals(account))
                .findFirst();
    }
    
    public Balance save(Balance balance) {
        if (balance.getId() == null) {
            balance.setId((long) (balanceStore.size() + 1));
        }
        balanceStore.put(balance.getId(), balance);
        return balance;
    }
}
