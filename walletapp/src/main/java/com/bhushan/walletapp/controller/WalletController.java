package com.bhushan.walletapp.controller;


import com.bhushan.walletapp.dto.*;
import com.bhushan.walletapp.entity.User;
import com.bhushan.walletapp.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basePath/v1/")
public class WalletController {

    @Autowired
    private WalletService walletService;

//    @PostMapping("users")
//    public ResponseEntity<WalletAccountResponseDto> createWalletAccount( @Valid  @RequestBody User user)
//    {
//        WalletAccountResponseDto walletAccountResponseDto=userService.createWalletAccount(user);
//        return new ResponseEntity<>(walletAccountResponseDto, HttpStatus.CREATED);
//    }

    @PostMapping("users")
   public ResponseEntity<EntityModel<WalletAccountResponseDto>> createWalletAccount(@Valid  @RequestBody User user)
   {
       WalletAccountResponseDto walletAccountResponseDto= walletService.createWalletAccount(user);
       EntityModel<WalletAccountResponseDto> entityModel=EntityModel.of(walletAccountResponseDto, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WalletController.class).getBalance(walletAccountResponseDto.getUserId())).withSelfRel());
       return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
   }

    @PatchMapping("users/wallet")
    public ResponseEntity<EntityModel<MessageResponse>> updateBalance(@RequestBody UpdateBalanceDto updateBalanceDto)
    {

        String res= walletService.updateBalance(updateBalanceDto);
        MessageResponse messageResponse=new MessageResponse(res);
        EntityModel<MessageResponse> response=EntityModel.of(messageResponse, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WalletController.class).getBalance(updateBalanceDto.getId())).withSelfRel());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("users/{userId}")
    public  ResponseEntity<EntityModel<WalletBalanceDto>> getBalance( @PathVariable("userId")  int userId)
    {
        WalletBalanceDto walletBalanceDto= walletService.checkBalance(userId);
        EntityModel<WalletBalanceDto> response=EntityModel.of(walletBalanceDto,WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WalletController.class).updateBalance(null)).withSelfRel());
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("users/wallet/transfer")
    public ResponseEntity<EntityModel<TransferResponseDto>> transfer(@RequestBody TransferBalanceDto transferBalanceDto)
    {
        TransferResponseDto transferResponseDto= walletService.transferAmount(transferBalanceDto);
        EntityModel<TransferResponseDto> response=EntityModel.of(transferResponseDto,WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WalletController.class).getBalance(transferResponseDto.getId())).withSelfRel());
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }
}
