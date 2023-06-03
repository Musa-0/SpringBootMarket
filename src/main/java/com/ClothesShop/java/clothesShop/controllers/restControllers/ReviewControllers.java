package com.ClothesShop.java.clothesShop.controllers.restControllers;

import com.ClothesShop.java.clothesShop.controllers.requestChecking.ReviewRequestChecker;
import com.ClothesShop.java.clothesShop.dao.DaoClass.HistoryOfProductButRepository;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.Review;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.services.ProsuctService;
import com.ClothesShop.java.clothesShop.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/reviews")
public class ReviewControllers {
    @Autowired
    ProsuctService prosuctService;
    @Autowired
    AccountService accountService;
    @Autowired
    HistoryOfProductButRepository historyOfProductButRepository;
    @Autowired
    ReviewService reviewService;

    ReviewRequestChecker reviewRequestChecker;

    @PostConstruct
    public void init() throws Exception
    {
        this.reviewRequestChecker = new ReviewRequestChecker(prosuctService, reviewService, historyOfProductButRepository);
    }

    @PostMapping("/create/{id}")
    public ResponseEntity create(@PathVariable("id") Long id, Integer review, Principal principal) {
        Map<Object, Object> dict = new HashMap();
        Account account = accountService.getByUsername(principal.getName());
        Product product = prosuctService.getById(id);
        ResponseEntity response = reviewRequestChecker.create_review(account, id, review);

        if(response==null){
            Review review_new = reviewService.create(product,review,account);
            dict.put("Отзыв оставлен", review_new);
            response = ResponseEntity.ok(dict);
        }

        return response;

    }

}
