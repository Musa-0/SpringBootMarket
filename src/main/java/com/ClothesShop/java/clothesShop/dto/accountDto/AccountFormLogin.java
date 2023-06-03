package com.ClothesShop.java.clothesShop.dto.accountDto;

import lombok.Data;

@Data
public class AccountFormLogin {         //класс который содержит в себе параметры запроса на вход
    private String username;
    private String password;
}
