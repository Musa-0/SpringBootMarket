package com.ClothesShop.java.clothesShop.services;

import com.ClothesShop.java.clothesShop.dao.DaoClass.AccountRepository;
import com.ClothesShop.java.clothesShop.dao.DaoClass.HistoryOfProductButRepository;
import com.ClothesShop.java.clothesShop.dao.DaoClass.ProductRespository;
import com.ClothesShop.java.clothesShop.dao.DaoInterface.ReviewJpaInterface;
import com.ClothesShop.java.clothesShop.dto.operationDto.PayProductDto;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Discount;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.HistoryBuy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OperationService {
    @Autowired
    ProductRespository productRespository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    HistoryOfProductButRepository historyOfProductButRepository;


    public Product add_discount(Product product, Discount discount) {
        product.setDiscount(discount);
        discount.setProduct(product);
        productRespository.save(product);
        return product;
    }

    public void delete_discount(Product product) {
        product.setDiscount(null);
        productRespository.save(product);
    }

    public void pay_product(Product product, Account account, PayProductDto payProductDto) {
        double amount = product.getPrice() * payProductDto.getCount();
        HistoryBuy historyBuy = new HistoryBuy();


        if(product.getDiscount()!=null){
            amount = amount * (1.0 - (float)product.getDiscount().getAmount()/100.0);
            historyBuy.setDiscount(product.getDiscount().getAmount());
        }
        System.out.println(amount);

        account.setBalance((int)(account.getBalance() - amount));
        product.setCount(product.getCount()-payProductDto.getCount());
        Account owner = accountRepository.findByOrganization(product.getOrganization());
        owner.setBalance((int) (owner.getBalance() + (amount * 0.95)));
        account.setCount_of_pay(account.getCount_of_pay()+1);

        accountRepository.save(account);
        accountRepository.save(owner);
        productRespository.save(product);

        historyBuy.setId(account.getCount_of_pay());
        historyBuy.setProduct_id(product.getId());
        historyBuy.setCount(payProductDto.getCount());
        historyBuy.setPrice(product.getPrice());
        historyBuy.setStatus("Куплено");
        historyBuy.setDate_of_pay(new Date());
        historyOfProductButRepository.set_history_product_of_pay(account,historyBuy);
    }

    public void return_product(Account account, Long id) {
        List<HistoryBuy> historyBuyList = historyOfProductButRepository.get_history_product_of_pay(account);
        HistoryBuy target = new HistoryBuy();
        for(HistoryBuy historyBuy: historyBuyList){
            if(historyBuy.getId().equals(id)){
                target = historyBuy;
            }
        }
        Product product = productRespository.findById(target.getProduct_id());

        Account owner = accountRepository.findByOrganization(product.getOrganization());
        product.setCount(product.getCount() + target.getCount());

        double amount = target.getCount()* target.getPrice();

        if(target.getDiscount()!=null){
            amount = amount * (1.0 - (float)target.getDiscount()/100.0);
        }
        target.setStatus("Возвращен");
        owner.setBalance((int)(owner.getBalance() - amount));
        account.setBalance((int)(account.getBalance() + amount));

        historyOfProductButRepository.set_history_product_of_pay_list(account,historyBuyList);
        accountRepository.save(account);
        accountRepository.save(owner);
        productRespository.save(product);
    }
}
