package com.bhushan.walletapp;


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
import com.bhushan.walletapp.service.WalletService;
import jakarta.validation.constraints.Min;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.module.ResolutionException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private User user;
    private Wallet wallet;
    private Currency currency;

    @BeforeEach
    public void setup()
    {
        user=new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("1234");
        user.setEmaild("test@test.com");

        currency=new Currency();
        currency.setId(1);
        currency.setCurrencyCd("INR");
        currency.setName("Indian Rupees");

        wallet=new Wallet();
        wallet.setId(1);
        wallet.setUser(user);
        wallet.setBalance(100.00);
        wallet.setCurrency(currency);

        user.setWallet(wallet);
    }

    @Test
    public void createWalletAccount_success()
    {
        User user=new User();
        user.setUsername("testUser");
        user.setEmaild("test@test.com");
        user.setPassword("1234");

        Currency currency= new Currency();

        currency.setId(1);
        currency.setCurrencyCd("INR");
        currency.setName("Indian Ruppes");
        when(userRepository.save(user)).thenReturn(user);
        when(currencyRepository.findById(1)).thenReturn(Optional.of(currency));
        WalletAccountResponseDto dto= walletService.createWalletAccount(user);
        assertNotNull(dto);
        assertEquals("Wallet Account Created Successfully!",dto.getContent());
        assertEquals(0.0, user.getWallet().getBalance());
        verify(userRepository).save(user);
        verify(currencyRepository).findById(1);

    }

    @Test
    public void updateBalance_shouldAddBalance()
    {
        UpdateBalanceDto updateBalanceDto=new UpdateBalanceDto();

        updateBalanceDto.setId(1);
        updateBalanceDto.setBalance(50.00);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        String result=walletService.updateBalance(updateBalanceDto);
        assertEquals("Balance Added successfully",result);
        assertEquals(150.00,user.getWallet().getBalance());

    }

    @Test
    public void checkBalance_Success()
    {
        when(walletRepository.findByUserId(1)).thenReturn(wallet);
        WalletBalanceDto walletBalanceDto=walletService.checkBalance(1);
        assertNotNull(walletBalanceDto);
        assertEquals(100,walletBalanceDto.getBalance());
        verify(walletRepository).findByUserId(1);

    }

    @Test
    public void checkBalance_WalletNotFound()
    {
        when(walletRepository.findByUserId(1)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->walletService.checkBalance(1));

    }

    @Test
    void transferAmount_success() {
        Wallet receiver = new Wallet();
        receiver.setId(2);
        receiver.setBalance(200.0);
        receiver.setUser(new User());

        TransferBalanceDto dto = new TransferBalanceDto();
        dto.setWalletId(1);
        dto.setToWalletId(2);
        dto.setTransferAmount(50.0);

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(walletRepository.findById(2)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(i -> {
                    Transaction t = i.getArgument(0);
                    t.setId(1);
                    return t;
                });

        TransferResponseDto response = walletService.transferAmount(dto);

        assertTrue(response.getContent().contains("Transfer done successfully"));
        verify(walletRepository, times(1)).save(wallet);
        verify(walletRepository, times(1)).save(receiver);
        verify(transactionRepository, atLeastOnce()).save(any(Transaction.class));
        assertEquals(50.0, wallet.getBalance());
        assertEquals(250.0, receiver.getBalance());
    }

    @Test
    void transferAmount_insufficientBalance_throws() {
        TransferBalanceDto dto = new TransferBalanceDto();
        dto.setWalletId(1);
        dto.setToWalletId(2);
        dto.setTransferAmount(500.0);

        Wallet receiver = new Wallet();
        receiver.setId(20);
        receiver.setBalance(0.0);
        receiver.setUser(new User());

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(walletRepository.findById(2)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(i -> i.getArgument(0));

        assertThrows(InsufficientBalanceException.class,
                () -> walletService.transferAmount(dto));

        // still saves a failed transaction
        verify(transactionRepository, atLeastOnce()).save(any(Transaction.class));
        // sender/receiver balances unchanged
        assertEquals(100.0, wallet.getBalance());
        assertEquals(0.0, receiver.getBalance());
    }



}
