package com.ClothesShop.java.clothesShop.models;

import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import com.ClothesShop.java.clothesShop.models.dopModels.OrganizationImage;
import com.ClothesShop.java.clothesShop.models.dopModels.ProductImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String title;
    @Column(name = "description")
    String description;
    @Column(name = "active")
    Boolean active;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "organizationImage_id")
    private OrganizationImage logo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization")
    @JsonIgnore
    private Account account;



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "organization")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();


}
