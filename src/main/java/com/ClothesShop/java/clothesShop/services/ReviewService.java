package com.ClothesShop.java.clothesShop.services;

import com.ClothesShop.java.clothesShop.dao.DaoInterface.ProductJpaInterface;
import com.ClothesShop.java.clothesShop.dao.DaoInterface.ReviewJpaInterface;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewJpaInterface reviewJpaInterface;
    @Autowired
    ProductJpaInterface productJpaInterface;

    public List<Review>findByAccount(Account account){
        return reviewJpaInterface.findByAccount(account);
    }

    public List<Review>findByProduct(Product product){
        return reviewJpaInterface.findByProduct(product);
    }

    public Review create(Product product, Integer review, Account account){
        List<Review> reviews = findByProduct(product);
        Integer count_reviews = reviews.size();
        Integer sum_reviews = 0;
        Review target = new Review();
        for(Review r: reviews){
            if(r.getAccount().equals(account)){
                target = r;
                sum_reviews-=r.getReview();
            }
            sum_reviews+=r.getReview();
        }
        if(target.getId()==null){
            target.setAccount(account);
            target.setProduct(product);
            count_reviews +=1;
        }
        target.setReview(review);
        product.setAvg_review((double)((float)(sum_reviews+review)/count_reviews));
        productJpaInterface.save(product);

        return reviewJpaInterface.save(target);

    }
}
