package com.bhushan.walletapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 5, max=10, message = "username must be minimum 5 characters and maximum upto 10 characters")
    private  String username;
    @Size(min = 8 ,max=10,message="password must be between 8 to 10 characters long")
    private String password;
    @Email(message = "Email id should be valid")
    private String emaild;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Wallet wallet;

    public void addWallet(Wallet w) {
        this.wallet = w;
        if (w != null) {
            w.setUser(this);
        }
    }

}
