package com.ClothesShop.java.clothesShop.controllers.restControllers;

import com.ClothesShop.java.clothesShop.controllers.requestChecking.ProductRequestChecker;
import com.ClothesShop.java.clothesShop.dto.productDto.ProductForm;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.HistoryBuy;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.services.ProsuctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductControllers {

    @Autowired
    ProsuctService prosuctService;
    @Autowired
    AccountService accountService;
    ProductRequestChecker productRequestChecker;

    @PostConstruct
    public void init() throws Exception
    {
        this.productRequestChecker = new ProductRequestChecker(prosuctService);
    }

    @GetMapping("/product/")
    public List<Product> getAll(){
        return prosuctService.getAll();

    }

    @GetMapping("/product/{id}")
    public ResponseEntity getById(@PathVariable(value = "id") Long id){
        Product product = prosuctService.getById(id);
        Map<Object,Object> response = new HashMap();
        if(product==null){
            response.put("Ошибка", "Нет такого товара");
            return ResponseEntity.ok(response);
        }
        response.put("Товар", product);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/update_image")
    public ResponseEntity update(ProductForm productForm,
                                  @RequestParam("files") MultipartFile[] files,
                                  @RequestParam ("add") String add, Principal principal){
        Account account = accountService.getByUsername(principal.getName());
        Product product = ProductForm.toProduct(productForm);
        ResponseEntity response = productRequestChecker.update_product_checker(account, product);
        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Product product_new = prosuctService.update(product, files, add);
            dict.put("product", product_new);
            response = ResponseEntity.ok(dict);
        }
        return response;
    }

    @PutMapping("/update")
    public ResponseEntity update(ProductForm productForm, Principal principal){
        Account account = accountService.getByUsername(principal.getName());
        Product product = ProductForm.toProduct(productForm);
        ResponseEntity response = productRequestChecker.update_product_checker(account, product);
        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Product product_new = prosuctService.update(product, null, "true");
            dict.put("product", product_new);
            response = ResponseEntity.ok(dict);
        }
        return response;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(ProductForm productForm, @RequestParam("files") MultipartFile[] files, Principal principal){
        Product product = ProductForm.toProduct(productForm);
        Account account = accountService.getByUsername(principal.getName());
        ResponseEntity response = productRequestChecker.create_product_chacker(account.getOrganization());

        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Product product_new = prosuctService.create(product, files, account.getOrganization());
            dict.put("Продукт", product_new);
            response = ResponseEntity.ok(dict);
        }
        return response;
    }

    @GetMapping("/history_product")
    public ResponseEntity get_history_product(Principal principal){
        Account account = accountService.getByUsername(principal.getName());
        List<HistoryBuy> historyBuyList = prosuctService.get_history(account);
        Map<Object, Object> response = new HashMap<>();

        response.put("История Покупок", historyBuyList);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id, Principal principal){
        Account account = accountService.getByUsername(principal.getName());
        ResponseEntity response = productRequestChecker.delete_product_checker(id,account);
        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            prosuctService.delete(id);
            dict.put("Статус", "продукт успешно удален");
            response = ResponseEntity.ok(dict);
        }
        return response;

    }


}
