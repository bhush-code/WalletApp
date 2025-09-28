package com.bhushan.walletapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double balance =0.0;

    @OneToOne
    @JoinColumn(name = "user_id" ,unique = true)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name ="currency_id", nullable = false)
    private Currency currency;

    @OneToMany(mappedBy ="fromWallet")
    private List<Transaction> incomingTransactions;

    @OneToMany(mappedBy ="toWallet")
    private List<Transaction> outgoingTransactions;
}

