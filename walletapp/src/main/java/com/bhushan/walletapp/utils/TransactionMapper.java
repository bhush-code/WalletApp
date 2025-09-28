package com.bhushan.walletapp.utils;

import com.bhushan.walletapp.dto.TransferBalanceDto;
import com.bhushan.walletapp.dto.WalletBalanceDto;
import com.bhushan.walletapp.entity.Transaction;
import com.bhushan.walletapp.entity.Wallet;

public class TransactionMapper {

    public static Transaction toEntity(TransferBalanceDto transferBalanceDto, Wallet fromWallet, Wallet toWallet, String status)
    {
        Transaction transaction=new Transaction();
        transaction.setAmount(transferBalanceDto.getTransferAmount());
        transaction.setFromWallet(fromWallet);
        transaction.setToWallet(toWallet);
        transaction.setStatus(status);
        return  transaction;

    }

}
