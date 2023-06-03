package com.ClothesShop.java.clothesShop.controllers.requestChecking;

import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Organization;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.services.ProsuctService;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ProductRequestChecker {

    ProsuctService productService;

    public ProductRequestChecker(ProsuctService procuctService) {
        this.productService = procuctService;
    }

    public ResponseEntity create_product_chacker(Organization organization){
        Map<Object, Object> response = new HashMap<>();

        if(organization==null){
            response.put("Ошибка", "У вас нет организации");
            return ResponseEntity.ok(response);
        }
        if(!organization.getActive()){
            response.put("Ошибка", "Ваша организация ещё не прошла модерацию");
            return ResponseEntity.ok(response);
        }
        return null;
    }

    public ResponseEntity delete_product_checker(Long id, Account account){
        Map<Object, Object> response = new HashMap<>();

        Product product = productService.getById(id);
        if(product==null) {
            response.put("Ошибка", "Нет такого товара");
            return ResponseEntity.badRequest().body(response);
        }
        if(account.getOrganization()==null){
            response.put("Ошибка","У вас нет организации");
            return ResponseEntity.ok(response);
        }
        if(!product.getOrganization().getId().equals(account.getOrganization().getId())){
            response.put("Ошибка","Это не ваш товар");
            return ResponseEntity.ok(response);
        }
        return null;
    }
    public ResponseEntity update_product_checker(Account account, Product product){
        Map<Object, Object> response = new HashMap<>();
        if(product.getId()==null){
            response.put("Ошибка","Вы не указали id товара");
            return ResponseEntity.badRequest().body(response);
        }
        Product last_product = productService.getById(product.getId());
        if(last_product==null) {
            response.put("Ошибка", "Нет такого товара");
            return ResponseEntity.badRequest().body(response);
        }
        if(account.getOrganization()==null){
            response.put("Ошибка","У вас нет организации");
            return ResponseEntity.ok(response);
        }
        if(!last_product.getOrganization().getId().equals(account.getOrganization().getId())){
            response.put("Ошибка","Это не ваш товар");
            return ResponseEntity.ok(response);
        }
        return null;
    }



}
