package com.ClothesShop.java.clothesShop.services.FileService;

import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import com.ClothesShop.java.clothesShop.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUploadService {

    private StorageService storageService;          //сервис работы с файлами

    public FileUploadService(StorageService storageService) {  //определим сервис работы с файлами
        this.storageService = storageService;
    }

    public ImageMedia uploadFile(MultipartFile file) {
        String name = storageService.store(file);
        String uri ="/media/" + name;

        return new ImageMedia(name, file.getSize(),file.getContentType(), uri);
    }


    public List<ImageMedia> uploadMultipleFiles(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
}
