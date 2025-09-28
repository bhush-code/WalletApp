package com.bhushan.walletapp.utils;

import com.bhushan.walletapp.dto.WalletBalanceDto;
import com.bhushan.walletapp.entity.Wallet;

public class WalletMapper {

    public static WalletBalanceDto toDto(Wallet wallet)
    {
        WalletBalanceDto walletBalanceDto=new WalletBalanceDto();

        walletBalanceDto.setUserId(wallet.getUser().getId());
        walletBalanceDto.setBalance(wallet.getBalance());
        walletBalanceDto.setCurrency(wallet.getCurrency().getCurrencyCd());

        return walletBalanceDto;
    }
}
