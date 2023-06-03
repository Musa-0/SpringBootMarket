package com.ClothesShop.java.clothesShop.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
//В application.properties файле мы определяем местоположение хранилища.
//Указывает класс чтобы автоматически привязать свойства, объявленные в application.properties файле.
@ConfigurationProperties(prefix = "storage")//prefix= "storage" привязывает все свойства, начинающиеся
// с storage префикса, к соответствующим им атрибутам класса StorageProperties при запуске приложения.
public class StorageProperties {

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
