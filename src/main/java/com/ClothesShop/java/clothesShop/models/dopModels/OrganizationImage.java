package com.ClothesShop.java.clothesShop.models.dopModels;

import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class OrganizationImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
            @JoinColumn(name = "image_id")
    ImageMedia imageMedia;
}
