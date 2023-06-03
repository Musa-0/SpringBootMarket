package com.ClothesShop.java.clothesShop.dto.operationDto;

import com.ClothesShop.java.clothesShop.models.Discount;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class DiscountDto {
    Integer amount;
    Integer days_expire;


    public static Discount toDiscount(DiscountDto discountDto){
        Discount discount = new Discount();
        discount.setAmount(discountDto.amount);
        Date dt = new Date();
        discount.setCreated(dt);
        if (discountDto.getDays_expire()!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, discountDto.days_expire);
            dt = c.getTime();
            discount.setExpire(dt);
        }
        return discount;

    }
}
