package com.bhushan.walletapp.service;

import com.bhushan.walletapp.dto.*;
import com.bhushan.walletapp.entity.Currency;
import com.bhushan.walletapp.entity.Transaction;
import com.bhushan.walletapp.entity.User;
import com.bhushan.walletapp.entity.Wallet;
import com.bhushan.walletapp.exception.InsufficientBalanceException;
import com.bhushan.walletapp.exception.ResourceNotFoundException;
import com.bhushan.walletapp.repo.CurrencyRepository;
import com.bhushan.walletapp.repo.TransactionRepository;
import com.bhushan.walletapp.repo.UserRepository;
import com.bhushan.walletapp.repo.WalletRepository;
import com.bhushan.walletapp.utils.TransactionMapper;
import com.bhushan.walletapp.utils.WalletAccountMapper;
import com.bhushan.walletapp.utils.WalletMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public WalletAccountResponseDto createWalletAccount(User user)
    {
        Currency currency=currencyRepository.findById(1).orElseThrow(()-> new RuntimeException("Currency Code is not present"));
        Wallet wallet=new Wallet();
//        wallet.setCurrencyId(1);
        wallet.setCurrency(currency);
        wallet.setBalance(0.0);
        user.addWallet(wallet);
             User walletCreated=userRepository.save(user);
             return WalletAccountMapper.toDto(walletCreated);
    }


    public String updateBalance(UpdateBalanceDto updateBalanceDto)
    {
        User user=userRepository.findById(updateBalanceDto.getId()).orElseThrow(()-> new RuntimeException("User not found"));

        Wallet wallet=user.getWallet();
        wallet.setBalance( wallet.getBalance()+updateBalanceDto.getBalance());
        userRepository.save(user);
        return "Balance Added successfully";
    }

    public WalletBalanceDto checkBalance(int userId)
    {
        Wallet wallet=walletRepository.findByUserId(userId);
        if(wallet==null)
        {
            throw  new ResourceNotFoundException("Wallet is not created for user id "+ userId);
        }
        WalletBalanceDto walletBalanceDto= WalletMapper.toDto(wallet);
        return walletBalanceDto;
    }

    @Transactional(dontRollbackOn = InsufficientBalanceException.class)
    public TransferResponseDto  transferAmount(TransferBalanceDto transferBalanceDto) {
        Wallet sender = walletRepository.findById(transferBalanceDto.getWalletId()).orElseThrow(() -> new ResourceNotFoundException("Wallet with id"+ transferBalanceDto.getWalletId()+" does not exists!!"));
        Wallet receiver = walletRepository.findById(transferBalanceDto.getToWalletId()).orElseThrow(() -> new ResourceNotFoundException("Wallet with id"+transferBalanceDto.getToWalletId()+" does not exists"));
        String status;
        TransferResponseDto transferResponseDto = new TransferResponseDto();
        if (transferBalanceDto.getTransferAmount() > sender.getBalance()) {

            status = "FAILED";
            Transaction failedTransaction=TransactionMapper.toEntity(transferBalanceDto,sender,receiver,status);
            transactionRepository.save(failedTransaction);
            throw new InsufficientBalanceException("Insufficient balance. Kindly review your balance");

        } else {
            sender.setBalance(sender.getBalance() - transferBalanceDto.getTransferAmount());
            receiver.setBalance(receiver.getBalance() + transferBalanceDto.getTransferAmount());
            status = "SUCCESS";
            walletRepository.save(sender);
            walletRepository.save(receiver);
        }
        Transaction transaction= TransactionMapper.toEntity(transferBalanceDto, sender,receiver,status);
        Transaction savedTransaction=transactionRepository.save(transaction);
        if(status.equals("SUCCESS"))
        {
            transferResponseDto.setContent("Transfer done successfully. Use transfer id "+savedTransaction.getId()+" for further reference");
            transferResponseDto.setId(sender.getUser().getId());
        }
        else transferResponseDto.setContent("Insufficient balance. Kindly review your balance");
        return transferResponseDto;
    }
}
