package com.bhushan.walletapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class TransferResponseDto {
    private String content;
    @JsonIgnore
    private int id;
}
