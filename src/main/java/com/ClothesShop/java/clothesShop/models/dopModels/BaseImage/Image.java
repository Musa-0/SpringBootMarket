package com.ClothesShop.java.clothesShop.models.dopModels.BaseImage;

import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Product;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "originalFileName")
    private String originalFileName;        //имя самого файла
    @Column(name = "size")
    private Long size;                      //размер файла
    @Column(name = "contentType")
    private String contentType;             //рассширение файла
    @Lob                                    //для хранения байт символов
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] bytes;                   //поток байт, а именно само изображение
    //ALTER TABLE images DROP COLUMN bytes;
    //ALTER TABLE images ADD COLUMN bytes bytea;    - поменм тип в таблице. это именно в postgres, чтобы передавать большие файлы

}