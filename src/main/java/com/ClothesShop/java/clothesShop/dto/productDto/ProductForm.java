package com.ClothesShop.java.clothesShop.dto.productDto;

import com.ClothesShop.java.clothesShop.models.Product;
import lombok.Data;

import javax.persistence.Column;

@Data
public class ProductForm {
    Long id;
    String title;
    String description;
    Integer price;
    Integer count;
    public static Product toProduct(ProductForm productForm){
        Product product = new Product();
        product.setId(productForm.getId());
        product.setTitle(productForm.getTitle());
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice());
        product.setCount(productForm.getCount());
        return product;
    }

}
