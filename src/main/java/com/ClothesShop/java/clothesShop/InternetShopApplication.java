package com.ClothesShop.java.clothesShop;

import com.ClothesShop.java.clothesShop.utils.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)  //врубаем наше хранилище файлов
public class InternetShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternetShopApplication.class, args);
	}

}
