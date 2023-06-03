package com.ClothesShop.java.clothesShop.controllers.restControllers;


import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import com.ClothesShop.java.clothesShop.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {                       //контроллер для работы с файлами

    private StorageService storageService;          //сервис работы с файлами

    public FileController(StorageService storageService) {  //определим сервис работы с файлами
        this.storageService = storageService;
    }

    @GetMapping("/media/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {       //скачивает файл на чужой комп

        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()      //возвращает файл закачивая его на комп
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
