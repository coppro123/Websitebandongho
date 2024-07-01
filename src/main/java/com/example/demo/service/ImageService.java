package com.example.demo.service;

import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private static final String UPLOAD_DIR = "uploads/images/";

    public List<Image> showListImageByProductId(long id){
        return  imageRepository.findByProductId(id);
    }

    public void deleteImageByProductId(Long id) {
        List<Image> imagesToDelete = imageRepository.findByProductId(id);
        imageRepository.deleteAll(imagesToDelete);
    }

    public void saveAllFilesList(List<Image> imageList) {
        // Save all the files into database
        for (Image item : imageList)
            imageRepository.save(item);
    }


}
