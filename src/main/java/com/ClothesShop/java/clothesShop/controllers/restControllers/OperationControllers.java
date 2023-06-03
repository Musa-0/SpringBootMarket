package com.ClothesShop.java.clothesShop.controllers.restControllers;

import com.ClothesShop.java.clothesShop.controllers.requestChecking.OperationRequestChecker;
import com.ClothesShop.java.clothesShop.dao.DaoClass.HistoryOfProductButRepository;
import com.ClothesShop.java.clothesShop.dto.operationDto.DiscountDto;
import com.ClothesShop.java.clothesShop.dto.operationDto.PayProductDto;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Discount;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.services.OperationService;
import com.ClothesShop.java.clothesShop.services.ProsuctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/operations")
public class OperationControllers {

    @Autowired
    ProsuctService prosuctService;
    @Autowired
    AccountService accountService;

    @Autowired
    OperationService operationService;

    @Autowired
    HistoryOfProductButRepository historyOfProductButRepository;

    OperationRequestChecker operationRequestChecker;

    @PostConstruct
    public void init() throws Exception
    {
        this.operationRequestChecker = new OperationRequestChecker(prosuctService, historyOfProductButRepository);
    }

    @PostMapping("/pay_product/{id}")
    public ResponseEntity pay_product(@PathVariable("id") Long id, PayProductDto payProductDto, Principal principal){
    Product product = prosuctService.getById(id);
    Account account = accountService.getByUsername(principal.getName());


    ResponseEntity response = operationRequestChecker.operation_pay_product(product,account,payProductDto);

    if(response==null) {
        Map<Object, Object> dict = new HashMap();
        try {
            operationService.pay_product(product, account, payProductDto);}
        catch (Exception e){
            dict.put("Статус операции","Произошла ошибка во время операции");
            e.printStackTrace();
            return ResponseEntity.ok(dict);}
        dict.put("Статус операции","Операция прошла успешно");
        response = ResponseEntity.ok(dict);
    }
    return response;
    }



    @PostMapping("/add_product_discount/{id}")
    public ResponseEntity add_discount(@PathVariable(value = "id") Long id, DiscountDto discountDto, Principal principal){
        Product product = prosuctService.getById(id);
        Account account = accountService.getByUsername(principal.getName());
        Discount discount = DiscountDto.toDiscount(discountDto);


        ResponseEntity response = operationRequestChecker.add_discount_product(product,account,discount);
        if(response==null) {
            Product product_discount = operationService.add_discount(product, discount);
            response = ResponseEntity.ok(product_discount);
        }
        return response;
    }

    @GetMapping("/return_product/{id}")
    public ResponseEntity return_product(@PathVariable("id") Long id, Principal principal){
        Map<Object, Object> dict = new HashMap();

        Account account = accountService.getByUsername(principal.getName());
        ResponseEntity response = operationRequestChecker.operation_return_product(account, id);
        if(response==null){
            operationService.return_product(account,id);
            dict.put("Статус операции","Операция прошла успешно");
            response = ResponseEntity.ok(dict);
        }
        return response;
    }

    @DeleteMapping("/delete_product_discount/{id}")
    public ResponseEntity discount_discount(@PathVariable(value = "id") Long id, Principal principal){
        Product product = prosuctService.getById(id);
        Account account = accountService.getByUsername(principal.getName());

        ResponseEntity response = operationRequestChecker.delete_discount_product(product, account);
        if(response==null) {
            operationService.delete_discount(product);
            response = ResponseEntity.ok("скидка успешно удалена");
        }
        return response;
    }
}
