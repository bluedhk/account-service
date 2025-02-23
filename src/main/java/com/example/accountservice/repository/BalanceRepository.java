package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends CrudRepository<Balance, Long> {
    Optional<Balance> findByAccount(Account account);
}
