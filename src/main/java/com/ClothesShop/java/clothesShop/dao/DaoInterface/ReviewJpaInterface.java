package com.ClothesShop.java.clothesShop.dao.DaoInterface;

import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJpaInterface extends JpaRepository<Review, Long> {
    List<Review> findByAccount(Account account);
    List<Review> findByProduct(Product product);
}
