package com.ClothesShop.java.clothesShop.dao.DaoInterface;

import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import com.ClothesShop.java.clothesShop.models.dopModels.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageProductJpaInterface extends JpaRepository<ProductImage, Long> {
}
