package com.example.accountservice.repository;

import com.example.accountservice.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AccountRepositoryStub implements AccountRepository {
    private final ConcurrentHashMap<Long, Account> accountStore = new ConcurrentHashMap<>();

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> entities) {
        entities.forEach(entity -> accountStore.put(entity.getId(), entity));
        return entities;
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountStore.get(id));
    }

    @Override
    public boolean existsById(Long aLong) {
        return findById(aLong).isPresent();
    }

    @Override
    public Iterable<Account> findAll() {
        return accountStore.values();
    }

    @Override
    public Iterable<Account> findAllById(Iterable<Long> longs) {
        List<Long> ids = new ArrayList<>();
        longs.forEach(ids::add);

        return accountStore.values().stream()
                .filter(account -> ids.contains(account.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public long count() {
        return accountStore.values().size();
    }

    @Override
    public void deleteById(Long aLong) {
        accountStore.remove(aLong);
    }

    @Override
    public void delete(Account entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Account> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }

    @Override
    public void deleteAll() {
        accountStore.clear();
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountStore.values().stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    @Override
    public List<Account> findAllAccounts() {
        return List.of();
    }

    public Account save(final Account account) {
        if (account.getId() == null || account.getId() == 0) {
            return createAccount(account);
        }
        return updateAccount(account);
    }

    private Account updateAccount(Account account) {
        accountStore.put(account.getId(), account);
        return account;
    }

    private Account createAccount(Account account) {
        final Long accountId = accountStore.size() + 1L;
        final Account saveAccount = Account.ofAll(
                accountId,
                account.getAccountNumber(),
                new ArrayList<>(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
        return updateAccount(saveAccount);
    }
}
