package com.projects.virtualDiary.controller;

import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import com.projects.virtualDiary.service.PhotoDiaryService;
import com.projects.virtualDiary.service.PhotoDiaryServiceStub;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api")
public class PhotoController {

    @Autowired
    private PhotoDiaryService photoDiaryService;

    @Setter
    private User user;

    @GetMapping("/user/me")
    public ResponseEntity<User> getUserDetails() {
        System.out.println("returning new user of data : "+ user);
        return ResponseEntity.ok(user); // Send only safe fields
    }

    // 🔹 User Login (Guest)
    @GetMapping("/auth/guestLogin")
    public ResponseEntity<User> guestLogin() {
        return ResponseEntity.ok(new User(0,"Guest", "Guest@gmial.com", "photo", false,new ArrayList<>()));
    }

    // 🔹 Fetch User Profile
    @GetMapping("/profile/{userId}")
    public ResponseEntity<List<UserCategories>> getUserCategories(@PathVariable String userId) {

        return photoDiaryService.getUserCategories(userId);
    }

    @GetMapping("/photos")
    public ResponseEntity<List<User>> getAllPhotos(@RequestParam(required = false) Long userId) {
        System.out.println("photo method called with userId: " + userId);
        List<User> sortedUsers = photoDiaryService.getAllUsers(userId);
        return ResponseEntity.ok(sortedUsers);
    }

    // 🔹 Fetch Individual Photo Details
    @GetMapping("/photo/{category}")
    public ResponseEntity<List<CategoryPhotos>> getPhotoDetails(@PathVariable Integer category) {
        System.out.println("photo method called");
        return photoDiaryService.getAllPhotos(category);
    }

    // 🔹 Upload Photo (Simulated)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("image") MultipartFile file, @RequestParam("userId") String userId) {
        return photoDiaryService.uploadPhoto(file, userId);
    }

    // 🔹 Delete Photo
    @DeleteMapping("/photo/Cloudinary/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable String photoId) {
        System.out.println(photoId);
        return photoDiaryService.deleteCollection(photoId);
    }

    @PostMapping("/{photoId}/uploadInnerPhoto")
    public ResponseEntity<String> uploadInnerPhoto(@PathVariable("photoId") String photoId,@RequestParam("image") MultipartFile file) {
        System.out.println("innerphoto method called");
        return photoDiaryService.uploadInnerPhoto(Integer.parseInt(photoId),file);
    }

    // 🔹 Delete Photo
    @DeleteMapping("/InnerPhoto/Cloudinary/{photoId}")
    public ResponseEntity<String> deleteInnerPhoto(@PathVariable String photoId) {
        System.out.println(photoId);
        return photoDiaryService.deletePhoto(photoId);
    }

    // 🔹 Fetch Individual Photo Details
    @PutMapping("/photo/{categoryId}/{title}")
    public ResponseEntity<String> updateCategoryText(@PathVariable Integer categoryId,@PathVariable String title) {
        System.out.println("updateCategoryText method called");
        return photoDiaryService.updateCategoryText(categoryId,title);
    }

    // 🔹 update Individual Photo lock
    @PutMapping("/photo/lock/{categoryId}/{lock}")
    public ResponseEntity<String> updateCategoryLcok(@PathVariable Integer categoryId,@PathVariable Boolean lock) {
        System.out.println("updateCategoryLock method called");
        return photoDiaryService.updateCategoryLcok(categoryId,lock);
    }

    // 🔹 update Individual Photo lock
    @PutMapping("/Innerphoto/lock/{PhotoId}/{lock}")
    public ResponseEntity<String> updatePhotoLcok(@PathVariable Integer PhotoId,@PathVariable Boolean lock) {
        System.out.println("updateCategoryLock method called");
        return photoDiaryService.updatePhotoLcok(PhotoId,lock);
    }

    // 🔹 update Individual Photo lock
    @PutMapping("/user/lock/{userId}/{lock}")
    public ResponseEntity<String> updateUserLock(@PathVariable Integer userId,@PathVariable Boolean lock) {
        System.out.println("updateCategoryLock method called");
        return photoDiaryService.updateUserLock(userId,lock);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        System.out.println("GetUser method called");
        return photoDiaryService.getUserById(userId);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, String>> getCategoryrById(@PathVariable Integer categoryId) {
        System.out.println("Getcategory method called");
        return photoDiaryService.getCategoryrById(categoryId);
    }

    @GetMapping("/photo/{categoryId}/user")
    public ResponseEntity<Integer> getCategoryrUser(@PathVariable Integer categoryId) {
        System.out.println("Getcategory method called");
        return photoDiaryService.getCategoryrUser(categoryId);
    }
}
