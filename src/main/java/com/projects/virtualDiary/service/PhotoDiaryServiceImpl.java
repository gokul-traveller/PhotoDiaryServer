package com.projects.virtualDiary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.projects.virtualDiary.Repo.UserCategoryRepository;
import com.projects.virtualDiary.Repo.UserPhotoRepository;
import com.projects.virtualDiary.Repo.UserRepository;
import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Profile({"default"})
public class PhotoDiaryServiceImpl implements PhotoDiaryService {

    private final Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCategoryRepository categoryRepository;
    @Autowired
    private UserPhotoRepository photoRepository;

    public PhotoDiaryServiceImpl(
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
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<List<UserCategories>> getUserCategories(String userId) {
        return ResponseEntity.ok(categoryRepository.findByUserIdOrdered(Integer.parseInt(userId)));
    }

    @Override
    public ResponseEntity<String> uploadPhoto(MultipartFile file, String userId) {
        try {
            Optional<User> user = userRepository.findById(Integer.parseInt(userId));
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "Cloudinary")); // optional folder
            String img = uploadResult.get("secure_url").toString();
            String pId = uploadResult.get("public_id").toString();
            System.out.println("public_id"+pId);
            categoryRepository.save(new UserCategories(pId,"Travel",img,false,user.orElse(null),null));
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
            UserCategories category = categoryRepository.findByPublicId("Cloudinary/" + photoId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));;
            List<CategoryPhotos> photos = category.getUserCategoryPhotos();
            for (CategoryPhotos photo : photos) {
                String publicId = photo.getPublicId(); // Make sure getter exists
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
            categoryRepository.delete(category);
            return ResponseEntity.ok(result.get("result").toString());
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @Override
    public ResponseEntity<List<CategoryPhotos>> getAllPhotos(int categoryId) {
        return ResponseEntity.ok(photoRepository.findByUserCategory_CategoryId(categoryId));
    }

    @Override
    public ResponseEntity<String> uploadInnerPhoto(int photoId, MultipartFile file) {
        try {
            Optional<UserCategories> photo = categoryRepository.findById(photoId);
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "Cloudinary")); // optional folder
            String img = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            System.out.println("public_id"+publicId);
            photoRepository.save(new CategoryPhotos(publicId,img,false,photo.orElse(null)));
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
            Optional<CategoryPhotos> photo = photoRepository.findByPublicId("Cloudinary/" + photoId);
            photo.ifPresent(photoRepository::delete);
            return ResponseEntity.ok(result.get("result").toString());
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @Override
    public ResponseEntity<String> updateCategoryText(Integer categoryId, String title) {
        try {
            Optional<UserCategories> category = categoryRepository.findById(categoryId);
            category.ifPresent(category1 -> category1.setName(title));
            assert category.orElse(null) != null;
            categoryRepository.save(category.orElse(null));
            return ResponseEntity.ok("Updated");
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }

}
