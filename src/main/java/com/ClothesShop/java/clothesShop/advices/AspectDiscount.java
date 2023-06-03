package com.ClothesShop.java.clothesShop.advices;


import com.ClothesShop.java.clothesShop.dao.DaoClass.ProductRespository;
import com.ClothesShop.java.clothesShop.models.Discount;
import com.ClothesShop.java.clothesShop.models.Product;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class AspectDiscount {
    @Autowired
    ProductRespository productRespository;


    @Around("execution(public com.ClothesShop.java.clothesShop.models.Product com.ClothesShop.java.clothesShop.services.ProsuctService.*(..))")
    public Product aspectDiscountExpireChecker(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        Product targetMethodResult = null;

        try {//если выкинет исключение, то мы можем его тут обработать, это очень удобно
            targetMethodResult = (Product) proceedingJoinPoint.proceed();//запускаем наш таргет метод от сюда
        }
        catch (Exception e){
            System.out.println("aspectDiscountExpireChecker: было поймано исключение " + e);
        }
        if(targetMethodResult!=null) {      //проверка скидки на его срок
            if (targetMethodResult.getDiscount() != null) {
                Date now = new Date();
                Discount discount = targetMethodResult.getDiscount();
                if(now.getTime() > discount.getExpire().getTime()){
                    targetMethodResult.setDiscount(null);
                    productRespository.save(targetMethodResult);
                }
            }
        }
        return targetMethodResult;    //возвращаем то что должен вернуть таргет метод
    }

    @Before("execution(public void com.ClothesShop.java.clothesShop.controllers.restControllers.OperationControllers.pay_product(..))")
    public void aspectDiscountExpire_BeforePayProductChecker(JoinPoint joinPoint) throws Throwable
    {
        Long id = (Long) joinPoint.getArgs()[0];
        Product product = productRespository.findById(id);
        if(product!=null) {
            if (product.getDiscount() != null) {
                Date now = new Date();
                Discount discount = product.getDiscount();
                if(now.getTime() > discount.getExpire().getTime()){
                    product.setDiscount(null);
                    productRespository.save(product);
                }
            }
        }
    }

}
