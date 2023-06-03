package com.ClothesShop.java.clothesShop.models.dopModels;

import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "review")
    Integer review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    Account account;



}
