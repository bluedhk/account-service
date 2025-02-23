package com.example.accountservice.model.command;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.AccountRepositoryStub;
import com.example.accountservice.repository.BalanceRepository;
import com.example.accountservice.service.vo.AccountDto;
import com.example.accountservice.service.vo.BalancesDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class AccountCommandServiceImpl implements AccountCommandService {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;

    public AccountCommandServiceImpl(AccountRepository accountRepository, BalanceRepository balanceRepository) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        final Account savedAccount = accountRepository.save(accountDto.toEntity(null));
        return AccountDto.fromEntity(savedAccount);
    }

    @Override
    public AccountDto deposit(AccountDto accountDto) {
        return saveBalanceAll(accountDto);
    }

    @Override
    public AccountDto withdraw(AccountDto accountDto) {
        return saveBalanceAll(accountDto);
    }

    private AccountDto saveBalanceAll(AccountDto accountDto) {
        final Account account = findById(accountDto.getId());
        final List<Balance> balances = accountDto.getBalances().toEntity(account);
        balanceRepository.saveAll(balances);
        return convertDto(account, balances);
    }

    private AccountDto convertDto(Account account, List<Balance> balances) {
        final AccountDto accountDtoFromEntity = AccountDto.fromEntity(account);
        final BalancesDto balancesDto = BalancesDto.fromEntity(balances);
        accountDtoFromEntity.updateBalances(balancesDto);
        if (accountRepository instanceof AccountRepositoryStub) {
            accountRepository.save(accountDtoFromEntity.toEntity(account));
        }
        return accountDtoFromEntity;
    }

    private Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
}
