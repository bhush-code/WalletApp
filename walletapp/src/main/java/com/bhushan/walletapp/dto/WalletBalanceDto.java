package com.bhushan.walletapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WalletBalanceDto {

    private int userId;
    private double balance;
    private String currency;
}
