package com.example.accountservice.service.vo;

import com.example.accountservice.model.Account;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountDto {
    private Long id;
    private String accountNumber;
    private BalancesDto balances;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account toEntity(Account account) {
        return Account.ofAll(
                this.id,
                this.accountNumber,
                this.balances.toEntity(account),
                this.createdAt,
                this.updatedAt
        );
    }

    public static AccountDto fromEntity(Account account) {
        if (account == null) {
            return null;
        }
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balances(BalancesDto.fromEntity(account.getBalances()))
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    public static AccountDto ofWithAccountNumberAndNoBalances(String accountNumber, LocalDateTime requestedAt) {
        return AccountDto.builder()
                .id(0L)
                .accountNumber(accountNumber)
                .balances(BalancesDto.empty())
                .createdAt(requestedAt)
                .updatedAt(requestedAt)
                .build();
    }

    public void deposit(BalanceDto balanceDto){
        final boolean hasBalanceType = balances.hasBalanceType(balanceDto.getBalanceType());
        if (!hasBalanceType) {
            balances.addBalance(balanceDto);
        }
        balances.deposit(balanceDto);
    }

    public void withdraw(BalanceDto balanceDto){
        balances.withdraw(balanceDto);
    }

    public boolean canWithdraw(BalanceDto balanceDto){
        return balances.canWithdraw(balanceDto);
    }

    public void updateBalances(BalancesDto balancesDto){
        this.balances = balancesDto;
    }
}
