package com.projects.virtualDiary.service;

import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

interface PhotoDiaryService {
    List<User> getAllUsers();
    ResponseEntity<List<UserCategories>> getUserCategories(String userId);
    ResponseEntity<String> uploadPhoto(MultipartFile file);
    ResponseEntity<String> deleteCollection(String photoId);
//    ResponseEntity<String> deletePhots(String photoId);
    ResponseEntity<List<CategoryPhotos>> getAllPhotos(Integer userId);
}
