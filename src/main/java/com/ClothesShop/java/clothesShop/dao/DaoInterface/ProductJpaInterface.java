package com.ClothesShop.java.clothesShop.dao.DaoInterface;

import com.ClothesShop.java.clothesShop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductJpaInterface extends JpaRepository<Product, Long> {

}
