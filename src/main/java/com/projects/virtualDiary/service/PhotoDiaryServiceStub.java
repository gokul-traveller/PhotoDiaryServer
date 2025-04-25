package com.projects.virtualDiary.service;

import com.cloudinary.utils.ObjectUtils;
import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.Photos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PhotoDiaryServiceStub implements PhotoDiaryService{

    private final Cloudinary cloudinary;

    String img1 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284939/Cloudinary/a5nkdy9xj71rrqix7qxt.jpg";
    String img2 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284419/Cloudinary/wpmke9rpoxjaiqxqnzuv.png";
    String img3 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744285018/Cloudinary/wcos1bztuuizzfngsqrv.jpg";

    List<User> users = new ArrayList<>();
    List<UserCategories> categories = new ArrayList<>();
    List<CategoryPhotos> photos = new ArrayList<>();
    Photos photo1 = new Photos("p1", img1, false);
    Photos photo2 = new Photos("p2", img3, false);
    Photos photo3 = new Photos("p3", img2, false);

    int couter =2;
    ClassPathResource imgFile = new ClassPathResource("images/abstractWallpaper.jpg");


    public PhotoDiaryServiceStub(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
        users.add(new User(1, "user1", "user1@gmial.com", img1, true));
        users.add(new User(2, "user2", "user2@gmial.com", img2, false));
        users.add(new User(3, "user3", "user3@gmial.com", img3, true));
        users.add(new User(4, "user4", "user4@gmial.com", img3, false));
        users.add(new User(5, "user5", "user5@gmial.com", img1, false));
        users.add(new User(6, "user6", "user6@gmial.com", img2, false));
        users.add(new User(7, "user7", "user7@gmial.com", img1, true));
        categories.add(new UserCategories("1","Travel",img3,false));
        photos.add(new CategoryPhotos(1,"Gokul",new Photos[]{photo1,photo2,photo3}));
    }

    @Override
    public List<User> getAllUsers() {

        // Convert image bytes to Base64
        try {

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return users;
    }
    @Override
    public ResponseEntity<List<UserCategories>> getUserCategories(String userId) {
        try {
            System.out.println(categories);
            return ResponseEntity.ok(categories);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> uploadPhoto(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "Cloudinary")); // optional folder
            String img = uploadResult.get("secure_url").toString();
            String pId = uploadResult.get("public_id").toString();
            System.out.println("public_id"+pId);
            categories.add(new UserCategories(pId,"Travel",img,false));
            return ResponseEntity.ok(img);
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    public ResponseEntity<String> deleteCollection(String photoId) {
        try {
            Map result = cloudinary.uploader().destroy("Cloudinary/"+photoId, ObjectUtils.emptyMap());
            System.out.println("photo id from react " + photoId);
            System.out.println(result);
            categories.removeIf(category -> category.getPhotoId().equals("Cloudinary/"+photoId));
            return ResponseEntity.ok(result.get("result").toString());
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @Override
    public ResponseEntity<List<CategoryPhotos>> getAllPhotos(Integer userId) {
        try {
            System.out.println(photos);
            return ResponseEntity.ok(photos);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }
}
