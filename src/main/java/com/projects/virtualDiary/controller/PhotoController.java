package com.projects.virtualDiary.controller;

import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import com.projects.virtualDiary.service.PhotoDiaryService;
import com.projects.virtualDiary.service.PhotoDiaryServiceStub;
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

    private static final List<User> users = new ArrayList<>();


    // 🔹 User Authentication (Simulated)
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        Optional<User> user = users.stream().filter(u -> u.getEmail().equals(email)).findFirst();

        if (user.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", String.valueOf(user.get().getUserId()));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
    }

    // 🔹 Fetch User Profile
    @GetMapping("/profile/{userId}")
    public ResponseEntity<List<UserCategories>> getUserCategories(@PathVariable String userId) {

        return photoDiaryService.getUserCategories(userId);
    }

    // 🔹 Fetch All Photos
    @GetMapping("/photos")
    public ResponseEntity<List<User>> getAllPhotos() {
        System.out.println("photo method called");
        return ResponseEntity.ok(photoDiaryService.getAllUsers());
    }

    // 🔹 Fetch Individual Photo Details
    @GetMapping("/photo/{category}")
    public ResponseEntity<List<CategoryPhotos>> getPhotoDetails(@PathVariable Integer category) {
        System.out.println("photo method called");
        return photoDiaryService.getAllPhotos(category);
    }

    // 🔹 Upload Photo (Simulated)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("image") MultipartFile file) {
        return photoDiaryService.uploadPhoto(file);
    }

    // 🔹 Delete Photo
    @DeleteMapping("/photo/Cloudinary/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable String photoId) {
        System.out.println(photoId);
        return photoDiaryService.deleteCollection(photoId);
    }

    // 🔹 Toggle Lock/Unlock Photo
    @PostMapping("/photo/{photoId}/toggle-lock")
    public ResponseEntity<Map<String, String>> toggleLockPhoto(@PathVariable int photoId) {
        for (User user : users) {
            if (user.getUserId() == photoId) {
                user.setLocked(!user.isLocked());
                return ResponseEntity.ok(Collections.singletonMap("message", "Photo lock status updated"));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{photoId}/uploadInnerPhoto")
    public ResponseEntity<String> uploadInnerPhoto(@PathVariable("photoId") int photoId,@RequestParam("image") MultipartFile file) {
        System.out.println("innerphoto method called");
        return photoDiaryService.uploadInnerPhoto(photoId,file);
    }

    // 🔹 Delete Photo
    @DeleteMapping("/InnerPhoto/Cloudinary/{photoId}")
    public ResponseEntity<String> deleteInnerPhoto(@PathVariable String photoId) {
        System.out.println(photoId);
        return photoDiaryService.deletePhoto(photoId);
    }
}
