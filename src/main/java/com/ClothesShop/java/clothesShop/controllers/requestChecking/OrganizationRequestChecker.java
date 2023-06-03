package com.ClothesShop.java.clothesShop.controllers.requestChecking;

import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.services.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class OrganizationRequestChecker {
    OrganizationService organizationService;
    
    public OrganizationRequestChecker(OrganizationService organizationService){
        this.organizationService = organizationService;
    }
    public ResponseEntity create_organization_checker(Account account){
        Map<Object, Object> response = new HashMap<>();
        if(account.getOrganization()!=null) {
            response.put("Уведомление", String.format("У вас уже есть организкация: %s", account.getOrganization().getTitle()));
            return ResponseEntity.ok(response);
        }
        return null;
    }
    public ResponseEntity update_delete_organization_checker(Account account){
        Map<Object, Object> response = new HashMap<>();

        if (account.getOrganization() == null) {
            response.put("Ошибка", "У вас нет организации");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        return null;
    }


}
