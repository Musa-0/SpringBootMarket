package com.ClothesShop.java.clothesShop.dto.accountDto;

import com.ClothesShop.java.clothesShop.models.Account;
import lombok.Data;
@Data
public class AccountFormChangePassword {
        String last_password;
        String new_password;
        String new_password_repeat;


}

