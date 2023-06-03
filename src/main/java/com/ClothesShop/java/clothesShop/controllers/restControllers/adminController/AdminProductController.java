package com.ClothesShop.java.clothesShop.controllers.restControllers.adminController;

import com.ClothesShop.java.clothesShop.dto.operationDto.DiscountDto;
import com.ClothesShop.java.clothesShop.models.Discount;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.services.OperationService;
import com.ClothesShop.java.clothesShop.services.OrganizationService;
import com.ClothesShop.java.clothesShop.services.ProsuctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    ProsuctService prosuctService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    OperationService operationService;



    @GetMapping("/product/all_products")
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
    public ResponseEntity update(Product product,
                                         @RequestParam("files") MultipartFile[] files,
                                         @RequestParam ("add") String add){
        if(product.getId()==null){
            return ResponseEntity.ok("Вы не указали id");
        }
        if(prosuctService.getById(product.getId())!=null) {
            Map<Object, Object> dict = new HashMap<>();
            Product product_new = prosuctService.update(product, files, add);
            dict.put("product", product_new);
            return ResponseEntity.ok(dict);
        }
        else {
            return ResponseEntity.ok("Нет такого объекта");
        }
    }

    @PutMapping("/update")
    public ResponseEntity update(Product product){
        if(product.getId()==null){
            return ResponseEntity.ok("Вы не указали id");
        }
        if(prosuctService.getById(product.getId())!=null) {
            Map<Object, Object> dict = new HashMap<>();
            Product product_new = prosuctService.update(product, null, "true");
            dict.put("product", product_new);
            return ResponseEntity.ok(dict);
        }
        else {
            return ResponseEntity.ok("Нет такого объекта");
        }
    }

    @PostMapping("/create/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(Product product, @PathVariable("id") Long id, @RequestParam("files") MultipartFile[] files){
            Map<Object, Object> dict = new HashMap<>();

            if(organizationService.getById(id)==null){
                return ResponseEntity.badRequest().body("Нет такой организации");
            }
            Product product_new = prosuctService.create(product, files, organizationService.getById(id));
            dict.put("Продукт", product_new);
            return ResponseEntity.ok(dict);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        Product product = prosuctService.getById(id);
        Map<Object, Object> dict = new HashMap<>();

        if(product!=null) {
            prosuctService.delete(id);
            dict.put("Статус", "продукт успешно удален");
            return ResponseEntity.ok(dict);
        }
        else {
            dict.put("Ошибка","Нет такого товара");
            return ResponseEntity.ok(dict);
        }
    }

    @PostMapping("/add_product_discount/{id}")
    public ResponseEntity add_discount(@PathVariable(value = "id") Long id, DiscountDto discountDto){
        Product product = prosuctService.getById(id);
        Discount discount = DiscountDto.toDiscount(discountDto);

        if(product==null) {
            Product product_discount = operationService.add_discount(product, discount);
            return ResponseEntity.ok(product_discount);
        }
        else {
            return ResponseEntity.ok("Нет такого товара");
        }
    }
    @DeleteMapping("/delete_product_discount/{id}")
    public ResponseEntity discount_discount(@PathVariable(value = "id") Long id){
        Product product = prosuctService.getById(id);
        if(product==null) {
            operationService.delete_discount(product);
            return ResponseEntity.ok("скидка успешно удалена");

        }
        else{
            return ResponseEntity.ok("Нет такого товара");
        }
    }
}
