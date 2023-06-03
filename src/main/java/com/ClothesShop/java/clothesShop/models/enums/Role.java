package com.ClothesShop.java.clothesShop.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role  implements GrantedAuthority {
    ADMIN, USER, STAFF;

    @Override
    public String getAuthority() {
        return name();
    }
}
