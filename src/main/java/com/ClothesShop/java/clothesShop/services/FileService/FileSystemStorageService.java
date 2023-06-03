package com.ClothesShop.java.clothesShop.services.FileService;

import com.ClothesShop.java.clothesShop.utils.StorageProperties;
import com.ClothesShop.java.clothesShop.exceptions.FileNotFoundException;
import com.ClothesShop.java.clothesShop.exceptions.StorageException;
import com.ClothesShop.java.clothesShop.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {           //сервис по загрузке файлов в файловую систему

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {                   //загрузка файла в файловую систему и выдача имени
        String filename = StringUtils.cleanPath(String.format("image-%d.%s",
                System.currentTimeMillis(),
                file.getContentType().split("/")[1]));
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {             //копируем файл в файловую систему
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return filename;        //возвращается имя файла
    }



    @Override                               //получения url файла
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override                                   //загрузка файла на комп
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {           //удаление всех файлов
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}