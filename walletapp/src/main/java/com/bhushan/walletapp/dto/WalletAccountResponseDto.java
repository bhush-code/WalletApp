package com.bhushan.walletapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletAccountResponseDto {

    private int userId;
    private  int walletId;
    private double balance;
    private String content;
}
