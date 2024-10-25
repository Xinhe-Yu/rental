package com.chatop.rental.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    public String saveBase64Image(String base64String) {
        try {
            String base64Image = base64String;
            if (base64String.contains(",")) {
                base64Image = base64String.split(",")[1];
            }

            String fileName = UUID.randomUUID().toString() + ".jpg";

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, imageBytes);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public boolean isBase64Image(String str) {
        try {
            new URL(str);
            return false;
        } catch (MalformedURLException e) {
            try {
                String base64String = str;
                if (str.contains(",")) {
                    base64String = str.split(",")[1];
                }
                Base64.getDecoder().decode(base64String);
                return true;
            } catch (IllegalAccessError ex) {
                return false;
            }
        }
    }
}
