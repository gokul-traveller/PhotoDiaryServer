package com.projects.virtualDiary.controller;

import com.projects.virtualDiary.model.Photo;
import com.projects.virtualDiary.model.User;
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
    private PhotoDiaryServiceStub photoDiaryServiceStub;

    private static final List<User> users = new ArrayList<>();
    private static final List<Photo> photos = new ArrayList<>();

    static {
        // Sample users
        users.add(new User("1", "John Doe", "john@example.com"));
        users.add(new User("2", "Alice Smith", "alice@example.com"));

        // Sample photos
        photos.add(new Photo(1, "1", "https://example.com/photo1.jpg", false, new ArrayList<>()));
        photos.add(new Photo(2, "2", "https://example.com/photo2.jpg", true, new ArrayList<>()));
    }

    // 🔹 User Authentication (Simulated)
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        Optional<User> user = users.stream().filter(u -> u.getEmail().equals(email)).findFirst();

        if (user.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.get().getId());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
    }

    // 🔹 Fetch User Profile
    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Fetch All Photos
    @GetMapping("/photos")
    public ResponseEntity<List<Photo>> getAllPhotos() {
        System.out.println("photo method called");
        return ResponseEntity.ok(photoDiaryServiceStub.getAllUsers());
    }

    // 🔹 Fetch Individual Photo Details
    @GetMapping("/photo/{photoId}")
    public ResponseEntity<Photo> getPhotoDetails(@PathVariable int photoId) {
        return photos.stream()
                .filter(photo -> photo.getId() == photoId)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Upload Photo (Simulated)
    @PostMapping("/photo/upload")
    public ResponseEntity<Map<String, String>> uploadPhoto(@RequestParam("userId") String userId, @RequestParam("file") MultipartFile file) {
        int newId = photos.size() + 1;
        photos.add(new Photo(newId, userId, "https://example.com/" + file.getOriginalFilename(), false, new ArrayList<>()));

        return ResponseEntity.ok(Collections.singletonMap("message", "Photo uploaded successfully"));
    }

    // 🔹 Delete Photo
    @DeleteMapping("/photo/{photoId}/delete")
    public ResponseEntity<Map<String, String>> deletePhoto(@PathVariable int photoId) {
        photos.removeIf(photo -> photo.getId() == photoId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Photo deleted successfully"));
    }

    // 🔹 Toggle Lock/Unlock Photo
    @PostMapping("/photo/{photoId}/toggle-lock")
    public ResponseEntity<Map<String, String>> toggleLockPhoto(@PathVariable int photoId) {
        for (Photo photo : photos) {
            if (photo.getId() == photoId) {
                photo.setLocked(!photo.isLocked());
                return ResponseEntity.ok(Collections.singletonMap("message", "Photo lock status updated"));
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 🔹 Grant Access to a Locked Photo
    @PostMapping("/photo/{photoId}/grant-access")
    public ResponseEntity<Map<String, String>> grantAccess(@PathVariable int photoId, @RequestBody Map<String, String> request) {
        String userEmail = request.get("userEmail");

        for (Photo photo : photos) {
            if (photo.getId() == photoId) {
                photo.getAccessList().add(userEmail);
                return ResponseEntity.ok(Collections.singletonMap("message", "Access granted successfully"));
            }
        }
        return ResponseEntity.notFound().build();
    }
}
