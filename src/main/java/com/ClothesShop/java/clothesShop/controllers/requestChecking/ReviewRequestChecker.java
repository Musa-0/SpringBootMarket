package com.ClothesShop.java.clothesShop.controllers.requestChecking;

import com.ClothesShop.java.clothesShop.dao.DaoClass.HistoryOfProductButRepository;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.HistoryBuy;
import com.ClothesShop.java.clothesShop.services.ProsuctService;
import com.ClothesShop.java.clothesShop.services.ReviewService;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewRequestChecker {
    ProsuctService productService;
    HistoryOfProductButRepository historyOfProductButRepository;
    ReviewService reviewService;

    public ReviewRequestChecker(ProsuctService procuctService, ReviewService reviewService, HistoryOfProductButRepository historyOfProductButRepository) {
        this.productService = procuctService;
        this.reviewService = reviewService;
        this.historyOfProductButRepository = historyOfProductButRepository;
    }

    public ResponseEntity create_review(Account account, Long id, Integer review) {
        Map<Object, Object> response = new HashMap<>();
        Product product = productService.getById(id);
        if(product==null){
            response.put("Ошибка", "Нет такого товара");
            return ResponseEntity.ok(response);
        }
        if(account.getOrganization()!=null){
            if(account.getOrganization().equals(product.getOrganization())){
                response.put("Ошибка", "Нельзя ставить отзыв на свой же товар");
                return ResponseEntity.ok(response);
            }
        }
        List<HistoryBuy> historyBuyList = historyOfProductButRepository.get_history_product_of_pay(account);
        HistoryBuy target = null;
        for(HistoryBuy historyBuy: historyBuyList){
            if (historyBuy.getProduct_id().equals(id)){
                target = historyBuy;
                break;
            }
        }
        if(target==null){
            response.put("Ошибка", "Вы не можете ставить отзыв за товар который не приоритали");
            return ResponseEntity.ok(response);
        }

        if(review>5 || review<1){
            response.put("Ошибка", "Оценка товара может быть от 1 до 5");
            return ResponseEntity.ok(response);
        }
        return null;
    }
}
