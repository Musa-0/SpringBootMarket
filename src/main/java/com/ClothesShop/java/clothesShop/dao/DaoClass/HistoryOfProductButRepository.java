package com.ClothesShop.java.clothesShop.dao.DaoClass;

import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.dopModels.HistoryBuy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class HistoryOfProductButRepository {

    @Value("${history.product.buy.location}")
    String path_to_files;

    public List<HistoryBuy> get_history_product_of_pay(Account account) {

        //объекты для первого способа
        List<HistoryBuy> historyBuyList = new ArrayList<>();
        try{
            File create_file = new File(String.format("%s/%d/history_product_buy", path_to_files, account.getId()));
            create_file.getParentFile().mkdirs();
            create_file.createNewFile();

            FileInputStream file = new FileInputStream(create_file);
            ObjectInputStream oos = new ObjectInputStream(file);

            historyBuyList = (List<HistoryBuy>) oos.readObject();
        }
        catch (ClassNotFoundException | IOException e) {}

        return historyBuyList;
    }


    public void set_history_product_of_pay(Account account, HistoryBuy historyBuy) {

        List<HistoryBuy> historyBuyList = get_history_product_of_pay(account);
        historyBuyList.add(historyBuy);

        try{
            File create_file = new File(String.format("%s/%d/history_product_buy",path_to_files, account.getId()));
            create_file.getParentFile().mkdirs();
            create_file.createNewFile();

            FileOutputStream fos = new FileOutputStream(create_file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(historyBuyList);
            oos.close();
        }
        catch (IOException e) {}
    }
    public void set_history_product_of_pay_list(Account account, List<HistoryBuy> historyBuyList) {

        try{
            File create_file = new File(String.format("%s/%d/history_product_buy",path_to_files, account.getId()));
            create_file.getParentFile().mkdirs();
            create_file.createNewFile();

            FileOutputStream fos = new FileOutputStream(create_file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(historyBuyList);
            oos.close();
        }
        catch (IOException e) {}
    }
}
