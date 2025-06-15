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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

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
    public List<User> getAllUsers(Long userId) {
        List<User> allUsers = userRepository.findAll();
        System.out.println("user ID : " + userId);
        if (userId != null) {
            allUsers.sort(Comparator.comparing(user -> user.getUserId() != userId));
        }
        System.out.println("sorted Users : "+ allUsers);
        return allUsers;

    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElse(null);
    }

    @Override
    public void saveUser(User user) {
        try{
            byte[] googleProfile = downloadImage(user.getImageData());
            Map uploadResult = cloudinary.uploader().upload(googleProfile,
                    ObjectUtils.asMap("folder", "Cloudinary")); // optional folder
            System.out.println("saved in cloudinary");
            String img = uploadResult.get("secure_url").toString();
            user.setImageData(img);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        userRepository.save(user);
    }

    public byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] data = new byte[1024];
            int nRead;
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }

    @Override
    public ResponseEntity<Integer> getCategoryrUser(int categoryId) {
        int userId = categoryRepository.findByCategoryId(categoryId).getUser().getUserId();
        return ResponseEntity.ok(userId);
    }

    @Override
    public ResponseEntity<List<UserCategories>> getUserCategories(String userId) {
        return ResponseEntity.ok(categoryRepository.findByUserIdOrdered(Integer.parseInt(userId)));
    }

    @Override
    public ResponseEntity<String> uploadPhoto(MultipartFile file, String userId) {
        try {
            System.out.println("user ID : " + userId);
            Optional<User> user = userRepository.findById(Integer.parseInt(userId));
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "Cloudinary")); // optional folder
            System.out.println("saved in cloudinary");
            String img = uploadResult.get("secure_url").toString();
            String pId = uploadResult.get("public_id").toString();
            System.out.println("public_id"+pId);
            categoryRepository.save(new UserCategories(pId,"Category",img,false,user.orElse(null),null));
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
            System.out.println("upload result begin");
            Optional<UserCategories> photo = categoryRepository.findById(photoId);
            System.out.println("photo is :"+ photo.toString());
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "Cloudinary"));
            System.out.println("upload result is :"+uploadResult.toString());// optional folder
            String img = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            System.out.println("public_id"+publicId);
            photoRepository.save(new CategoryPhotos(publicId,img,false,photo.orElse(null)));
            return ResponseEntity.ok(img);
        }
        catch (Exception e){
            System.out.println("upload result is :"+e); // This will show the complete error details
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

    @Override
    public ResponseEntity<String> updateCategoryLcok(Integer categoryId, boolean lock) {
        try {
            Optional<UserCategories> category = categoryRepository.findById(categoryId);
            category.ifPresent(category1 -> category1.setLocked(lock));
            assert category.orElse(null) != null;
            categoryRepository.save(category.orElse(null));
            return ResponseEntity.ok("Updated");
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }

    @Override
    public ResponseEntity<String> updatePhotoLcok(Integer photoId, boolean lock) {
        try {
            Optional<CategoryPhotos> photo = photoRepository.findById(photoId);
            photo.ifPresent(photo1 -> photo1.setLocked(lock));
            assert photo.orElse(null) != null;
            photoRepository.save(photo.orElse(null));
            return ResponseEntity.ok("Updated");
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }

    @Override
    public ResponseEntity<String> updateUserLock(Integer userId, boolean lock) {
        try {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(photo1 -> photo1.setLocked(lock));
            assert user.orElse(null) != null;
            userRepository.save(user.orElse(null));
            return ResponseEntity.ok("Updated");
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }

    @Override
    public ResponseEntity<User> getUserById(int userId) {
        return userRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Map<String, String>> getCategoryrById(int categoryId) {
        UserCategories category = categoryRepository.findById(categoryId).orElse(null);
        assert category != null;
        User user = category.getUser();
        String userName = user.getUserName();
        String categoryName = category.getName();
        Map<String, String> result = new HashMap<>();
        result.put("userName", userName);
        result.put("categoryName", categoryName);
        return ResponseEntity.ok(result);
    }

}
