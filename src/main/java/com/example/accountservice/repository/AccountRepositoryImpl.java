package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.QAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QAccount account = QAccount.account;

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(
                queryFactory.selectFrom(account)
                        .where(account.accountNumber.eq(accountNumber))
                        .fetchOne()
        );
    }

    @Override
    public List<Account> findAllAccounts() {
        return queryFactory.selectFrom(account)
                .fetch();
    }
}