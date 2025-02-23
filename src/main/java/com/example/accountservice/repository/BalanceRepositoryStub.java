package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BalanceRepositoryStub implements BalanceRepository {
    private final ConcurrentHashMap<Long, Balance> balanceStore = new ConcurrentHashMap<>();
    
    public Optional<Balance> findByAccount(Account account) {
        return balanceStore.values().stream()
                .filter(balance -> balance.getAccount().equals(account))
                .findFirst();
    }

    public List<Balance> saveAll(List<Balance> balances) {
        return balances.stream()
                .map(this::save)
                .toList();
    }

    public Balance save(Balance balance) {
        if (balance.getId() == null || balance.getId() == 0) {
            return createBalance(balance);
        }
        return updateBalance(balance);
    }

    private Balance updateBalance(Balance balance) {
        balanceStore.put(balance.getId(), balance);
        return balance;
    }

    private Balance createBalance(Balance balance) {
        final Long balanceId = balanceStore.size() + 1L;
        final Balance saveBalance = Balance.ofAll(balanceId, balance.getBalanceType(), balance.getAccount(), balance.getAmount());
        return updateBalance(saveBalance);
    }

    @Override
    public <S extends Balance> Iterable<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return entities;
    }

    @Override
    public Optional<Balance> findById(Long aLong) {
        return Optional.ofNullable(balanceStore.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return findById(aLong).isPresent();
    }

    @Override
    public Iterable<Balance> findAll() {
        return balanceStore.values();
    }

    @Override
    public Iterable<Balance> findAllById(Iterable<Long> longs) {
        List<Long> ids = new ArrayList<>();
        longs.forEach(ids::add);
        return balanceStore.values().stream()
                .filter(balance -> ids.contains(balance.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public long count() {
        return balanceStore.values().size();
    }

    @Override
    public void deleteById(Long aLong) {
        balanceStore.remove(aLong);
    }

    @Override
    public void delete(Balance entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Balance> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }

    @Override
    public void deleteAll() {
        balanceStore.clear();
    }
}
