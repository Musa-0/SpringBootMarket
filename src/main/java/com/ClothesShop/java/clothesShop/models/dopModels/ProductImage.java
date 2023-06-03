package com.ClothesShop.java.clothesShop.models.dopModels;

import com.ClothesShop.java.clothesShop.models.Organization;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "isPreviewImage")
    private boolean isPreviewImage;         //та фотография которая будет отображаться в списке товаров. из трех фото

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    ImageMedia imageMedia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public ProductImage(ImageMedia imageMedia, Product product) {
        this.imageMedia = imageMedia;
        this.product = product;
    }
    public ProductImage(Boolean isPreviewImage,ImageMedia imageMedia, Product product) {
        this.imageMedia = imageMedia;
        this.product = product;
        this.isPreviewImage = isPreviewImage;
    }
}
