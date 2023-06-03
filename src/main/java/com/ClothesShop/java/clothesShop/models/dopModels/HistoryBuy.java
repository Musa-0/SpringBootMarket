package com.ClothesShop.java.clothesShop.models.dopModels;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HistoryBuy implements Serializable {
    private static final long serialVersionUID = 753328943;

    Long id;
    Long product_id;
    Integer count;
    Integer price;
    Integer discount;
    Date date_of_pay;
    String status;


}
