package com.projects.virtualDiary.service;

import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PhotoDiaryService {
    List<User> getAllUsers();
    ResponseEntity<List<UserCategories>> getUserCategories(String userId);
    ResponseEntity<String> uploadPhoto(MultipartFile file);
    ResponseEntity<String> deleteCollection(String photoId);
    ResponseEntity<List<CategoryPhotos>> getAllPhotos(int userId);
    ResponseEntity<String> uploadInnerPhoto(int photoId,MultipartFile file);
    ResponseEntity<String> deletePhoto(String photoId);
    void addInitialValues(List<User> users,List<UserCategories> categories,List<CategoryPhotos> photos);
}
