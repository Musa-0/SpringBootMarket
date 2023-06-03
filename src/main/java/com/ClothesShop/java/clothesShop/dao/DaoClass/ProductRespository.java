package com.ClothesShop.java.clothesShop.dao.DaoClass;

import com.ClothesShop.java.clothesShop.dao.DaoInterface.OrganizationJpaInterface;
import com.ClothesShop.java.clothesShop.dao.DaoInterface.ProductJpaInterface;
import com.ClothesShop.java.clothesShop.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ProductRespository {
    @Autowired
    ProductJpaInterface productJpaInterface;

    @Autowired//создадим менеджер общения с бд, вместо сессий как было в hibernate
    private EntityManager entityManager;

    public List<Product> findAll() {
        return productJpaInterface.findAll();
    }

    public Product findById(Long id) {
        return productJpaInterface.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productJpaInterface.save(product);
    }

    public void deleteById(Long id) {
        productJpaInterface.deleteById(id);
    }
}
