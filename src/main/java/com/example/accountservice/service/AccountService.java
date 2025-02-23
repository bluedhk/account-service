package com.example.accountservice.service;

import com.example.accountservice.common.exception.AccountServiceException;
import com.example.accountservice.model.command.AccountCommandService;
import com.example.accountservice.model.query.AccountQueryService;
import com.example.accountservice.service.vo.AccountDto;
import com.example.accountservice.service.vo.BalanceDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {
    private final AccountQueryService accountQueryService;
    private final AccountCommandService accountCommandService;

    public AccountService(AccountQueryService accountQueryService, AccountCommandService accountCommandService) {
        this.accountQueryService = accountQueryService;
        this.accountCommandService = accountCommandService;
    }

    public AccountDto getAccountBalance(Long accountId) {
        return accountQueryService.findById(accountId);
    }

    public AccountDto createAccount(final String accountNumber) {
        final LocalDateTime requestedAt = LocalDateTime.now();
        final AccountDto account = AccountDto.ofWithAccountNumberAndNoBalances(accountNumber, requestedAt);
        return accountCommandService.createAccount(account);
    }

    public AccountDto deposit(Long accountId, BalanceDto balanceDto) {
        validateDepositAmount(balanceDto);
        return processDeposit(accountId, balanceDto);
    }

    private AccountDto processDeposit(Long accountId, BalanceDto balanceDto) {
        final AccountDto accountDto = findAccountIfNotExistsRaiseException(accountId);
        accountDto.deposit(balanceDto);
        return accountCommandService.deposit(accountDto);
    }

    private static void validateDepositAmount(BalanceDto balanceDto) {
        final boolean canDeposit = balanceDto.canDeposit();
        if (!canDeposit) {
            throw new AccountServiceException("Deposit amount must be greater than zero.");
        }
    }

    private AccountDto findAccountIfNotExistsRaiseException(Long accountId) {
        final AccountDto accountDto = accountQueryService.findById(accountId);
        if (accountDto == null) {
            throw new AccountServiceException("Account not found");
        }
        return accountDto;
    }

    public AccountDto withdraw(Long accountId, BalanceDto balanceDto) {
        final AccountDto accountDto = findAccountIfNotExistsRaiseException(accountId);
        validateWithdrawAmount(balanceDto, accountDto);
        return processWithdraw(balanceDto, accountDto);
    }

    private AccountDto processWithdraw(BalanceDto balanceDto, AccountDto accountDto) {
        accountDto.withdraw(balanceDto);
        return accountCommandService.withdraw(accountDto);
    }

    private void validateWithdrawAmount(BalanceDto balanceDto, AccountDto accountDto) {
        final boolean canWithdraw = accountDto.canWithdraw(balanceDto);
        if (!canWithdraw) {
            throw new AccountServiceException("Withdraw amount must be greater than zero.");
        }
    }
}
