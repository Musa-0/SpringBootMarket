package com.ClothesShop.java.clothesShop.models;

import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.Image;
import com.ClothesShop.java.clothesShop.models.dopModels.OrganizationImage;
import com.ClothesShop.java.clothesShop.models.dopModels.ProductImage;
import com.ClothesShop.java.clothesShop.models.dopModels.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "price")
    Integer price;

    @Column(name = "count")
    Integer count;

    @Column(name = "avg_review")
    Double avg_review;

    @Column(name = "active")
    Boolean active;
    public Product(){}

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy = "product")
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_id")
    private Discount discount;


}
