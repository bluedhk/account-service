package com.example.accountservice.model.command;

import com.example.accountservice.service.vo.AccountDto;

public interface AccountCommandService {
    AccountDto createAccount(AccountDto accountDto);

    AccountDto deposit(AccountDto accountDto);
    AccountDto withdraw(AccountDto accountDto);
}
