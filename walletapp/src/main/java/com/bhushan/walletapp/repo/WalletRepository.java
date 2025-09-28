package com.bhushan.walletapp.repo;

import com.bhushan.walletapp.dto.WalletBalanceDto;
import com.bhushan.walletapp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByUserId(int userId);
}
