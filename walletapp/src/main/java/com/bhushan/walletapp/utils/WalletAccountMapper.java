package com.bhushan.walletapp.utils;

import com.bhushan.walletapp.dto.WalletAccountResponseDto;
import com.bhushan.walletapp.entity.User;
import com.bhushan.walletapp.entity.Wallet;

public class WalletAccountMapper {

    public  static WalletAccountResponseDto toDto(User user)
    {
//        Wallet wallet=new Wallet();
//        wallet.setBalance(0.0);
//        wallet.setCurrencyId(1);
//        user.setWallet(wallet);
        WalletAccountResponseDto walletAccountResponseDto=new WalletAccountResponseDto();
        walletAccountResponseDto.setUserId(user.getId());
        walletAccountResponseDto.setBalance(user.getWallet().getBalance());
        walletAccountResponseDto.setWalletId(user.getWallet().getId());
        walletAccountResponseDto.setContent("Wallet Account Created Successfully!");
        return  walletAccountResponseDto;
    }
}
