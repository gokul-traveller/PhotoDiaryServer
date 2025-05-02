package com.projects.virtualDiary.service;

import com.projects.virtualDiary.Repo.UserCategoryRepository;
import com.projects.virtualDiary.Repo.UserPhotoRepository;
import com.projects.virtualDiary.Repo.UserRepository;
import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@Profile({"default"})
public class PhotoDiaryServiceImpl implements PhotoDiaryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCategoryRepository categoryRepository;
    @Autowired
    private UserPhotoRepository photoRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<List<UserCategories>> getUserCategories(String userId) {
        return ResponseEntity.ok(categoryRepository.findByUserId(Integer.parseInt(userId)));
    }

    @Override
    public ResponseEntity<String> uploadPhoto(MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteCollection(String photoId) {
        return null;
    }

    @Override
    public ResponseEntity<List<CategoryPhotos>> getAllPhotos(int categoryId) {
        return ResponseEntity.ok(photoRepository.findByCategoryId(categoryId));
    }

    @Override
    public ResponseEntity<String> uploadInnerPhoto(int photoId, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<String> deletePhoto(String photoId) {
        return null;
    }

    public void addInitialValues(List<User> users,List<UserCategories> categories,List<CategoryPhotos> photos) {
        userRepository.saveAll(users);
        categoryRepository.saveAll(categories);
        photoRepository.saveAll(photos);
    }
}
