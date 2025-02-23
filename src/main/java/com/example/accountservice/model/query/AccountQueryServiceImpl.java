package com.example.accountservice.model.query;

import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.service.vo.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountQueryServiceImpl implements AccountQueryService {
    private final AccountRepository accountRepository;

    public AccountQueryServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto findById(Long id) {
        return AccountDto.fromEntity(accountRepository.findById(id).orElse(null));
    }
}
