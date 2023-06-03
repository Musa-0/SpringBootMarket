package com.ClothesShop.java.clothesShop.dto.accountDto;

import com.ClothesShop.java.clothesShop.models.Account;
import lombok.Data;

import javax.persistence.Column;

@Data
public class AccountFormRegister {
    String username;
    String email;
    String password;

    public static Account toAccount(AccountFormRegister accountFormRegister){
        Account account = new Account();
        account.setUsername(accountFormRegister.getUsername());
        account.setEmail(accountFormRegister.getEmail());
        account.setPassword(accountFormRegister.getPassword());
        return account;
    }
    public static AccountFormRegister toAccountFormRegister(Account account){
        AccountFormRegister accountFormRegister = new AccountFormRegister();
        accountFormRegister.setUsername(account.getUsername());
        accountFormRegister.setEmail(account.getEmail());
        accountFormRegister.setPassword(account.getPassword());
        return accountFormRegister;
    }
}
