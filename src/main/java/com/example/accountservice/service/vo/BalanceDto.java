package com.example.accountservice.service.vo;

import com.example.accountservice.model.Account;
import com.example.accountservice.model.Balance;
import com.example.accountservice.service.enums.BalanceType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceDto {
    private Long id;
    private BalanceType balanceType;
    private BigDecimal amount;

    public Balance toEntity(Account account) {
        return Balance.ofAll(
                this.id,
                this.balanceType.name(),
                account,
                this.amount
        );
    }

    public boolean canDeposit() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean canWithdraw(BigDecimal amount) {
        return this.amount.compareTo(amount) >= 0;
    }

    public boolean equalBalanceType(BalanceType balanceType) {
        return this.balanceType.equals(balanceType);
    }

    public void deposit(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }

    public static BalanceDto fromEntity(Balance balance) {
        return BalanceDto.builder()
                .id(balance.getId())
                .balanceType(BalanceType.valueOf(balance.getBalanceType()))
                .amount(balance.getAmount())
                .build();
    }

    public static BalanceDto from(BalanceType balanceType) {
        return BalanceDto.builder()
                .id(0L)
                .balanceType(balanceType)
                .amount(BigDecimal.ZERO)
                .build();
    }
}
