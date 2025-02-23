package com.example.accountservice.service;

import com.example.accountservice.common.exception.AccountServiceException;
import com.example.accountservice.model.command.AccountCommandService;
import com.example.accountservice.model.command.AccountCommandServiceImpl;
import com.example.accountservice.model.query.AccountQueryService;
import com.example.accountservice.model.query.AccountQueryServiceImpl;
import com.example.accountservice.repository.AccountRepositoryStub;
import com.example.accountservice.repository.BalanceRepositoryStub;
import com.example.accountservice.service.enums.BalanceType;
import com.example.accountservice.service.vo.AccountDto;
import com.example.accountservice.service.vo.BalanceDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    private final AccountRepositoryStub accountRepositoryStub = new AccountRepositoryStub();
    private final BalanceRepositoryStub balanceRepositoryStub = new BalanceRepositoryStub();

    private final AccountQueryService accountQueryService = new AccountQueryServiceImpl(accountRepositoryStub);
    private final AccountCommandService accountCommandService = new AccountCommandServiceImpl(accountRepositoryStub, balanceRepositoryStub);

    private final AccountService accountService = new AccountService(accountQueryService, accountCommandService);

    @Test
    void getAccountBalanceTest() {
        // given
        final String accountNumber = "accountNumber";
        final AccountDto savedAccount = accountService.createAccount(accountNumber);
        // when
        final AccountDto result = accountService.getAccountBalance(savedAccount.getId());
        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals(savedAccount.getId(), result.getId());
    }

    @Test
    void createAccountTest() {
        // given
        final String accountNumber = "accountNumber";
        // when
        final AccountDto result = accountService.createAccount(accountNumber);
        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
    }

    @Test
    void depositSuccessTest() {
        // given
        final String accountNumber = "accountNumber";
        final BigDecimal amount = new BigDecimal("100");
        final AccountDto savedAccount = accountService.createAccount(accountNumber);
        final BalanceDto requestBalanceDto = BalanceDto.from(BalanceType.STANDARD);
        requestBalanceDto.deposit(amount);
        // when
        final AccountDto result = accountService.deposit(savedAccount.getId(), requestBalanceDto);
        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals(savedAccount.getId(), result.getId());
        assertEquals(result.getBalances().getBalances().get(0).getAmount(), amount);
    }

    @Test
    void depositMinusAmountFailTest() {
        // given
        final String accountNumber = "accountNumber";
        final BigDecimal amount = new BigDecimal("-100");
        final AccountDto savedAccount = accountService.createAccount(accountNumber);
        final BalanceDto requestBalanceDto = BalanceDto.from(BalanceType.STANDARD);
        requestBalanceDto.deposit(amount);
        // then
        assertThrows(AccountServiceException.class, () -> accountService.deposit(savedAccount.getId(), requestBalanceDto));
    }

    @Test
    void withdrawSuccessTest() {
        // given
        final String accountNumber = "accountNumber";
        final BigDecimal amount = new BigDecimal("100");
        final AccountDto savedAccount = accountService.createAccount(accountNumber);
        final BalanceDto requestBalanceDto = BalanceDto.from(BalanceType.STANDARD);
        requestBalanceDto.deposit(amount);
        final AccountDto depositedAccountDto = accountService.deposit(savedAccount.getId(), requestBalanceDto);
        // when
        final AccountDto result = accountService.withdraw(savedAccount.getId(), requestBalanceDto);
        // then
        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals(savedAccount.getId(), result.getId());
        assertEquals(result.getBalances().getBalances().get(0).getAmount(), BigDecimal.ZERO);
    }

    @Test
    void withdrawOverAmountFailTest() {
        // given
        final String accountNumber = "accountNumber";
        final BigDecimal amount = new BigDecimal("100");
        final AccountDto savedAccount = accountService.createAccount(accountNumber);
        final BalanceDto requestBalanceDto = BalanceDto.from(BalanceType.STANDARD);
        requestBalanceDto.deposit(amount);
        final AccountDto depositedAccountDto = accountService.deposit(savedAccount.getId(), requestBalanceDto);
        requestBalanceDto.deposit(amount.add(BigDecimal.ONE));
        // then
        assertThrows(AccountServiceException.class, () -> accountService.withdraw(savedAccount.getId(), requestBalanceDto));
    }
}