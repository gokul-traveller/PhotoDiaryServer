package com.projects.virtualDiary.service;

import com.cloudinary.utils.ObjectUtils;
import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;

import java.util.*;

@Service
@Profile({"dev"})
public class PhotoDiaryServiceStub implements PhotoDiaryService{

    private final Cloudinary cloudinary;

    String img1 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284939/Cloudinary/a5nkdy9xj71rrqix7qxt.jpg";
    String img2 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284419/Cloudinary/wpmke9rpoxjaiqxqnzuv.png";
    String img3 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744285018/Cloudinary/wcos1bztuuizzfngsqrv.jpg";

    List<User> users = new ArrayList<>();
    List<UserCategories> categories = new ArrayList<>();
    CategoryPhotos photos;
//    Photos photo1 = new Photos(1,"publicId", img1, false);
//    Photos photo2 = new Photos(2,"publicId", img3, false);
//    Photos photo3 = new Photos(3,"publicId", img2, false);

    int couter =10;
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
//            categories.add(new UserCategories(0,new User(1, "user1", "user1@gmial.com", img1, true,categories),pId,"Travel",img,false));
            return ResponseEntity.ok(img);
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @Override
    public ResponseEntity<String> deleteCollection(String photoId) {
        try {
            Map result = cloudinary.uploader().destroy("Cloudinary/"+photoId, ObjectUtils.emptyMap());
            System.out.println("photo id from react " + photoId);
            System.out.println(result);
            categories.removeIf(category -> category.getPublicId().equals("Cloudinary/"+photoId));
            return ResponseEntity.ok(result.get("result").toString());
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @Override
    public ResponseEntity<List<CategoryPhotos>> getAllPhotos(int userId) {
        try {
            System.out.println(photos);
            return ResponseEntity.ok(new ArrayList<>(Arrays.asList(photos)));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> uploadInnerPhoto(int photoId,MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "Cloudinary")); // optional folder
            int Id = couter++;
            String img = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            System.out.println("public_id"+publicId);
//            photos.getPhotos().add(new Photos(Id,publicId,img,false));
            return ResponseEntity.ok(img);
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @Override
    public ResponseEntity<String> deletePhoto(String photoId) {
        try {
            Map result = cloudinary.uploader().destroy("Cloudinary/"+photoId, ObjectUtils.emptyMap());
            System.out.println("photo id from react " + photoId);
            System.out.println(result);
//            photos.getPhotos().removeIf(photo -> photo.getPublicId().equals("Cloudinary/"+photoId));
            return ResponseEntity.ok(result.get("result").toString());
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @Override
    public void addInitialValues(List<User> users, List<UserCategories> categories, List<CategoryPhotos> photos) {
        //do nothing
    }
}
