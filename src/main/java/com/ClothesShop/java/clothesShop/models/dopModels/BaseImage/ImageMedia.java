package com.ClothesShop.java.clothesShop.models.dopModels.BaseImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "size")
    private Long size;                      //размер файла
    @Column(name = "contentType")
    private String contentType;             //рассширение файла
    @Column(name = "url")
    private String url;

    public ImageMedia(String name, Long size, String contentType, String url) {
        this.name = name;
        this.size = size;
        this.contentType = contentType;
        this.url = url;
    }
}
