package com.ClothesShop.java.clothesShop.controllers.requestChecking;

import com.ClothesShop.java.clothesShop.dao.DaoClass.HistoryOfProductButRepository;
import com.ClothesShop.java.clothesShop.dto.operationDto.PayProductDto;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Discount;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.HistoryBuy;
import com.ClothesShop.java.clothesShop.services.ProsuctService;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class OperationRequestChecker {

    ProsuctService productService;
    HistoryOfProductButRepository historyOfProductButRepository;

    public OperationRequestChecker(ProsuctService procuctService, HistoryOfProductButRepository historyOfProductButRepository) {
        this.productService = procuctService;
        this.historyOfProductButRepository = historyOfProductButRepository;
    }

    public ResponseEntity operation_pay_product(Product product, Account account, PayProductDto payProductDto) {
        Map<Object, Object> response = new HashMap<>();

        if(product==null){
            response.put("Ошибка", "Нет такого товара");
            return ResponseEntity.ok(response);
        }

        if(product.getOrganization().equals(account.getOrganization())){
            response.put("Ошибка", "Вы не можете купить свой же товар");
            return ResponseEntity.ok(response);
        }

        if(product.getActive().equals(false) || product.getOrganization().getActive().equals(false)){
            response.put("Ошибка", "Данный товар или организация заблокированы");
            return ResponseEntity.ok(response);
        }

        if(payProductDto.getCount()<1 || payProductDto.getCount()==null){
            response.put("Ошибка", "вы не указали колличество товара");
            return ResponseEntity.ok(response);
        }

        if(product.getCount()<payProductDto.getCount()){
            response.put("Ошибка", "У продавца нет в наличии нужного колличества");
            return ResponseEntity.ok(response);
        }

        Integer amount = product.getPrice() * payProductDto.getCount();
        if(product.getDiscount()!=null){
            amount = amount * (1 - product.getDiscount().getAmount()/100);
        }

        if(account.getBalance()<amount){
            response.put("Ошибка", "У вас недостаточно средств");
            return ResponseEntity.ok(response);
        }

        return null;

    }

    public ResponseEntity add_discount_product(Product product, Account account, Discount discount){
        Map<Object, Object> response = new HashMap<>();

        if(product==null){
            response.put("Ошибка", "Нет такого товара");
            return ResponseEntity.ok(response);
        }

        if(!product.getOrganization().equals(account.getOrganization())){
            response.put("Ошибка", "Это не ваш товар");
            return ResponseEntity.ok(response);
        }

        if(discount.getExpire()==null){
            response.put("Ошибка", "Вы не указали срок действия скидки");
            return ResponseEntity.ok(response);
        }
        if(discount.getAmount()==null){
            response.put("Ошибка", "Вы не указали размер скидки");
            return ResponseEntity.ok(response);
        }
        if(discount.getAmount()>99 || discount.getAmount()<1){
            response.put("Ошибка", "Диапазон скидки может быть от 1% до 99%");
            return ResponseEntity.ok(response);
        }
        return null;
    }

    public ResponseEntity delete_discount_product(Product product, Account account) {
        Map<Object, Object> response = new HashMap<>();

        if(product==null){
            response.put("Ошибка", "Нет такого товара");
            return ResponseEntity.ok(response);
        }

        if(!product.getOrganization().equals(account.getOrganization())){
            response.put("Ошибка", "Это не ваш товар");
            return ResponseEntity.ok(response);
        }

        return null;
    }



    public ResponseEntity operation_return_product(Account account, Long id) {
        List<HistoryBuy> historyBuyList = historyOfProductButRepository.get_history_product_of_pay(account);

        HistoryBuy target = null;
        for(HistoryBuy historyBuy: historyBuyList){
            if (historyBuy.getId().equals(id)){
                target = historyBuy;
                break;
            }
        }
        if(target==null){
            return ResponseEntity.badRequest().body("Нет такого продукта");
        }

        if (target.getStatus().equals("Возвращен")){
            return ResponseEntity.badRequest().body("Вы уже вернули этот товар");
        }

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        dt = c.getTime();

        if(target.getDate_of_pay().getTime()<dt.getTime()){
            return ResponseEntity.ok("Срок действия возврата истек. Вы можете вернуть товар лишь в течении суток после покупки");
        }
        Product product = productService.getById(target.getProduct_id());
        if(product==null){
            return ResponseEntity.ok("Данный товар удален пользователем");
        }
        return null;
    }
}
