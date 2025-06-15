package com.projects.virtualDiary.service;

import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

public interface PhotoDiaryService {
    List<User> getAllUsers(Long userId);
    ResponseEntity<List<UserCategories>> getUserCategories(String userId);
    ResponseEntity<String> uploadPhoto(MultipartFile file, String userId);
    ResponseEntity<String> deleteCollection(String photoId);
    ResponseEntity<List<CategoryPhotos>> getAllPhotos(int userId);
    ResponseEntity<String> uploadInnerPhoto(int photoId,MultipartFile file);
    ResponseEntity<String> deletePhoto(String photoId);
    ResponseEntity<String> updateCategoryText(Integer categoryId, String title);
    ResponseEntity<String> updateCategoryLcok(Integer categoryId, boolean lock);
    ResponseEntity<String> updatePhotoLcok(Integer photoId, boolean lock);
    ResponseEntity<String> updateUserLock(Integer userId, boolean lock);
    ResponseEntity<User> getUserById(int userId);
    ResponseEntity<Map<String, String>> getCategoryrById(int categoryId);
    User getUserByEmail(String userEmail);
    void saveUser(User user);
    ResponseEntity<Integer> getCategoryrUser(int categoryId);
}
