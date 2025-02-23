package com.example.accountservice.model.query;

import com.example.accountservice.service.vo.AccountDto;

public interface AccountQueryService {
    AccountDto findById(Long id);
}
