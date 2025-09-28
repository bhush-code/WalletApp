package com.bhushan.walletapp.dto;

import lombok.Data;

@Data
public class TransferBalanceDto {

    private int walletId;
    private int toWalletId;
    private double transferAmount;
}
