package com.example.accountservice.service.vo;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.service.enums.BalanceType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BalancesDto {
    private List<BalanceDto> balances;
    public List<Balance> toEntity(Account account) {
        if (account == null) {
            return new ArrayList<>();
        }
        return balances.stream()
                .map(balanceDto -> balanceDto.toEntity(account))
                .toList();
    }

    public static BalancesDto fromEntity(List<Balance> balances) {
        if (balances.isEmpty()) {
            return BalancesDto.empty();
        }
        return BalancesDto.builder()
                .balances(balances.stream()
                        .map(BalanceDto::fromEntity)
                        .toList())
                .build();
    }

    public static BalancesDto empty() {
        return BalancesDto.builder()
                .balances(new ArrayList<>())
                .build();
    }

    public boolean hasBalanceType(BalanceType balanceType) {
        return balances.stream().anyMatch(balanceDto -> balanceDto.equalBalanceType(balanceType));
    }

    public void addBalance(BalanceDto balanceDto) {
        balances.add(BalanceDto.from(balanceDto.getBalanceType()));
    }

    public void deposit(BalanceDto balanceDto) {
        balances.stream()
                .filter(filteredBalanceDto -> filteredBalanceDto.equalBalanceType(balanceDto.getBalanceType()))
                .findAny().ifPresent(findBalance -> findBalance.deposit(balanceDto.getAmount()));
    }

    public void withdraw(BalanceDto balanceDto) {
        balances.stream()
                .filter(filteredBalanceDto -> filteredBalanceDto.equalBalanceType(balanceDto.getBalanceType()))
                .findAny().ifPresent(findBalance -> findBalance.withdraw(balanceDto.getAmount()));
    }

    public boolean canWithdraw(BalanceDto balanceDto) {
        return balances.stream()
                .filter(filteredBalanceDto -> filteredBalanceDto.equalBalanceType(balanceDto.getBalanceType()))
                .anyMatch(filteredBalanceDto -> filteredBalanceDto.canWithdraw(balanceDto.getAmount()));
    }
}
