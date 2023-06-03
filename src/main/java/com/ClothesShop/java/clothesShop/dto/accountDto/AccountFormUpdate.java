package com.ClothesShop.java.clothesShop.dto.accountDto;

import com.ClothesShop.java.clothesShop.models.Account;
import lombok.Data;

@Data
public class AccountFormUpdate {
    String username;
    String email;

    public static Account toAccount(AccountFormUpdate accountFormUpdate){
        Account account = new Account();
        account.setUsername(accountFormUpdate.getUsername());
        account.setEmail(accountFormUpdate.getEmail());
        return account;
    }
}
