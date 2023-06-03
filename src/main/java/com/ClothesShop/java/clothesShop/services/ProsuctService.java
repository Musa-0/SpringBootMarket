package com.ClothesShop.java.clothesShop.services;

import com.ClothesShop.java.clothesShop.dao.DaoClass.HistoryOfProductButRepository;
import com.ClothesShop.java.clothesShop.dao.DaoClass.ProductRespository;
import com.ClothesShop.java.clothesShop.dao.DaoInterface.ImageProductJpaInterface;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Organization;
import com.ClothesShop.java.clothesShop.models.Product;
import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import com.ClothesShop.java.clothesShop.models.dopModels.HistoryBuy;
import com.ClothesShop.java.clothesShop.models.dopModels.ProductImage;
import com.ClothesShop.java.clothesShop.services.FileService.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ProsuctService {

    @Autowired
    ProductRespository productRespository;

    @Autowired
    private FileUploadService uploadService;

    @Autowired
    ImageProductJpaInterface imageProductJpaInterface;

    @Autowired
    HistoryOfProductButRepository historyOfProductButRepository;


    public List<Product> getAll(){
        return productRespository.findAll().stream().filter(e -> e.getActive()).collect(Collectors.toList());
    }

    public Product getById(Long id){
        return productRespository.findById(id);
    }

    public Product create(Product product, MultipartFile[] files, Organization organization){
        Product product_with_file = create_files(product, files);
        product.setOrganization(organization);
        product.setActive(true);
        return productRespository.save(product_with_file);
    }


    public Product update(Product product, MultipartFile[] files, String add){
        Product last_product = getById(product.getId());
        Product emity_product = new Product();
        Class last_product_ref = Product.class;

        for(Field f: last_product_ref.getDeclaredFields()){
            try {
                f.setAccessible(true);
                if(f.get(product)!=null){
                    if(!f.get(product).equals(f.get(emity_product))){
                        f.set(last_product, f.get(product));
                        }
                    }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(files!=null){
            if(!add.equals("true")){
                delete_files(last_product);
            }
            last_product = create_files(last_product, files);
        }
        return productRespository.save(last_product);



    }



    public void delete(Long id){
        Product product = productRespository.findById(id);
        delete_files(product);
        productRespository.deleteById(id);
    }


    public Product create_files(Product product, MultipartFile[] files){
        List<ImageMedia> images = uploadService.uploadMultipleFiles(files);
        AtomicBoolean isPreviewImage = new AtomicBoolean(true);
        List<ProductImage> productImages = images.stream()
                .map(file -> {
                    if(isPreviewImage.get()) {
                        isPreviewImage.set(false);
                        return new ProductImage(true,file, product);}

                    else {
                        return new ProductImage(file, product);
                    }
                }).collect(Collectors.toList());
        product.setImages(productImages);
        return product;
    }

    public void delete_files(Product product){
        List<ProductImage> productImages = product.getImages();
        product.setImages(new ArrayList<>());
        for(ProductImage i: productImages){
            Path file = Paths.get("uploads/" + i.getImageMedia().getName());
            try {
                Files.delete(file);
                imageProductJpaInterface.delete(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productRespository.save(product);


    }


    public List<HistoryBuy> get_history(Account account) {
        return historyOfProductButRepository.get_history_product_of_pay(account);
    }
}
