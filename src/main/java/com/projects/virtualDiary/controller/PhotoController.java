package com.projects.virtualDiary.controller;

import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import com.projects.virtualDiary.service.CustomUserDetails;
import com.projects.virtualDiary.service.JwtService;
import com.projects.virtualDiary.service.PhotoDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoDiaryService photoDiaryService;
    @Autowired
    JwtService jwtService;

    @GetMapping("/auth/guestLogin")
    public ResponseEntity<?> guestLogin() {
        User guestUser = new User(0, "Guest", "guest@gmail.com", "photo", false, new ArrayList<>());

        // 🔐 Generate JWT token (adjust method as per your JWT implementation)
        String token = jwtService.generateToken(guestUser.getUserEmail()); // Replace with your actual method

        // 📦 Return both user and token
        Map<String, Object> response = new HashMap<>();
        response.put("user", guestUser);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }


    // 🔹 Fetch User Profile
    @GetMapping("/profile/{userId}")
    public ResponseEntity<List<UserCategories>> getUserCategories(@PathVariable String userId) {
        return photoDiaryService.getUserCategories(userId);
    }

    // 🔹 Get all users (filtered if userId provided)
    @GetMapping("/photos")
    public ResponseEntity<List<User>> getAllPhotos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println("inside getallphotos");
        System.out.println(userDetails);
        if (userDetails == null) {
            throw new RuntimeException("CustomUserDetails is null!");
        }
        System.out.println("before calling service of  getallphotos");
        int userId = userDetails.getUserId();
        return ResponseEntity.ok(photoDiaryService.getAllUsers(userId));
    }

    // 🔹 Fetch photos under a category
    // need upgrade
    @GetMapping("/photo/{category}")
    public ResponseEntity<List<CategoryPhotos>> getPhotoDetails(@PathVariable Integer category) {
        return photoDiaryService.getAllPhotos(category);
    }

    // 🔹 Upload Photo to a category
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("image") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new RuntimeException("CustomUserDetails is null!");
        }
        int userId = userDetails.getUserId();
        return photoDiaryService.uploadPhoto(file, userId);
    }

    // 🔹 Delete a top-level category photo
    @DeleteMapping("/photo/Cloudinary/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable String photoId) {
        return photoDiaryService.deleteCollection(photoId);
    }

    // 🔹 Upload inner photo to a category
    @PostMapping("/{photoId}/uploadInnerPhoto")
    public ResponseEntity<String> uploadInnerPhoto(
            @PathVariable("photoId") String photoId,
            @RequestParam("image") MultipartFile file
    ) {
        return photoDiaryService.uploadInnerPhoto(Integer.parseInt(photoId), file);
    }

    // 🔹 Delete an inner photo
    @DeleteMapping("/InnerPhoto/Cloudinary/{photoId}")
    public ResponseEntity<String> deleteInnerPhoto(@PathVariable String photoId) {
        return photoDiaryService.deletePhoto(photoId);
    }

    // 🔹 Update category title
    @PutMapping("/photo/{categoryId}/{title}")
    public ResponseEntity<String> updateCategoryText(
            @PathVariable Integer categoryId,
            @PathVariable String title
    ) {
        return photoDiaryService.updateCategoryText(categoryId, title);
    }

    // 🔹 Lock/unlock category
    @PutMapping("/photo/lock/{categoryId}/{lock}")
    public ResponseEntity<String> updateCategoryLock(
            @PathVariable Integer categoryId,
            @PathVariable Boolean lock
    ) {
        return photoDiaryService.updateCategoryLcok(categoryId, lock);
    }

    // 🔹 Lock/unlock inner photo
    @PutMapping("/Innerphoto/lock/{photoId}/{lock}")
    public ResponseEntity<String> updatePhotoLock(
            @PathVariable Integer photoId,
            @PathVariable Boolean lock
    ) {
        return photoDiaryService.updatePhotoLcok(photoId, lock);
    }

    // 🔹 Lock/unlock user
    @PutMapping("/user/lock/{lock}")
    public ResponseEntity<String> updateUserLock(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Boolean lock
    ) {
        if (userDetails == null) {
            throw new RuntimeException("CustomUserDetails is null!");
        }
        int userId = userDetails.getUserId();
        return photoDiaryService.updateUserLock(userId, lock);
    }

    // 🔹 Fetch user by ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        return photoDiaryService.getUserById(userId);
    }

    // 🔹 Fetch category text/title by categoryId
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, String>> getCategoryById(@PathVariable Integer categoryId) {
        return photoDiaryService.getCategoryrById(categoryId);
    }

    // 🔹 Fetch owner userId of a category
    @GetMapping("/photo/{categoryId}/user")
    public ResponseEntity<Boolean> getCategoryOwner(@PathVariable Integer categoryId) {
        return photoDiaryService.getCategoryrUser(categoryId);
    }
}
