package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByAccount(Account account);
}
