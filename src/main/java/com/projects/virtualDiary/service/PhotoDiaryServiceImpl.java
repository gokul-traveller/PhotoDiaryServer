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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PhotoDiaryServiceImpl implements PhotoDiaryService {

    private Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCategoryRepository userCategoriesRepository;

    @Autowired
    private UserPhotoRepository categoryPhotosRepository;

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

    private String getCurrentUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        return authentication.getName();
    }

    private Optional<User> isUserOwner(String userEmail, int userId) {
        return userRepository.findById(userId)
                .filter(user -> user.getUserEmail().equals(userEmail));
    }

    private boolean isCategoryOwner(String userEmail, int categoryId) {
        return userCategoriesRepository.findById(categoryId)
                .map(cat -> cat.getUser().getUserEmail().equals(userEmail))
                .orElse(false);
    }

    private Optional<UserCategories> isCategoryOwner(String userEmail, String categoryId) {
        return userCategoriesRepository.findByPublicId("Cloudinary/"+ categoryId)
                .filter(cat -> cat.getUser().getUserEmail().equals(userEmail));
    }

    private Optional<CategoryPhotos> isPhotoOwner(String userEmail, String photoId) {
        return categoryPhotosRepository.findByPublicId("Cloudinary/"+ photoId)
                .filter(photo -> photo.getUserCategory()
                        .getUser()
                        .getUserEmail()
                        .equals(userEmail));
    }

    @Override
    public List<User> getAllUsers(int userId) {
        List<User> allUsers = userRepository.findAll();
        System.out.println("user ID : " + userId);
        if (userId <= 0) {
            return allUsers; // don't sort for guest user
        }
        allUsers.sort((u1, u2) -> {
            if (u1.getUserId() == userId) return -1;
            if (u2.getUserId() == userId) return 1;
            return 0; // keep relative order of others unchanged
        });

        System.out.println("sorted Users : " + allUsers);
        return allUsers;
    }


    @Override
    public ResponseEntity<List<UserCategories>> getUserCategories(String userId) {
        String email = getCurrentUserEmail();
        Optional<User> targetUser = userRepository.findById(Integer.parseInt(userId));

        if (!targetUser.isPresent()) return ResponseEntity.notFound().build();
        User user = targetUser.get();

        if (user.isLocked() && !user.getUserEmail().equals(email)) {
            return ResponseEntity.status(403).build();
        }

        List<UserCategories> categories = userCategoriesRepository.findByUserIdOrdered(user.getUserId());
        return ResponseEntity.ok(categories);
    }

    @Override
    public ResponseEntity<String> uploadPhoto(MultipartFile file, int userId) {
        try {
            Optional<User> user = isUserOwner(getCurrentUserEmail(), userId);
            if (!user.isPresent()) {
                return ResponseEntity.status(403).body("Unauthorized to upload");
            } else {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("folder", "Cloudinary")); // optional folder
                System.out.println("saved in cloudinary");
                String img = uploadResult.get("secure_url").toString();
                String pId = uploadResult.get("public_id").toString();
                System.out.println("public_id" + pId);
                userCategoriesRepository.save(new UserCategories(pId, "Category", img, false, user.orElse(null), null));
                return ResponseEntity.ok(img);
            }
        }
        catch (Exception e){
                e.printStackTrace(); // This will show the complete error details
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @Override
    public ResponseEntity<String> deleteCollection(String categoryId) {
        try {
            String email = getCurrentUserEmail();
            System.out.println(isCategoryOwner(email, categoryId));
            Optional<UserCategories> optionalCategory = isCategoryOwner(email, categoryId);

            if (optionalCategory.isPresent()) {
                UserCategories category = optionalCategory.get();
                Map result = cloudinary.uploader().destroy("Cloudinary/"+categoryId, ObjectUtils.emptyMap());
                System.out.println("photo id from react " + categoryId);
                System.out.println(result);
                List<CategoryPhotos> photos = category.getUserCategoryPhotos();
                for (CategoryPhotos photo : photos) {
                    String publicId = photo.getPublicId(); // Make sure getter exists
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                }
                userCategoriesRepository.delete(category);
                return ResponseEntity.ok(result.get("result").toString());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
            }
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @Override
    public ResponseEntity<List<CategoryPhotos>> getAllPhotos(int categoryId) {
        String email = getCurrentUserEmail();
        Optional<UserCategories> categoryOpt = userCategoriesRepository.findById(categoryId);
        if (!categoryOpt.isPresent()) return ResponseEntity.notFound().build();

        UserCategories category = categoryOpt.get();

        if (category.isLocked() && !category.getUser().getUserEmail().equals(email)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(categoryPhotosRepository.findByUserCategory_CategoryId(categoryId));
    }

    @Override
    public ResponseEntity<String> uploadInnerPhoto(int categoryId, MultipartFile file) {
        try {
            if (!isCategoryOwner(getCurrentUserEmail(), categoryId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to update");
            }
            System.out.println("upload result begin");
            Optional<UserCategories> photo = userCategoriesRepository.findById(categoryId);
            System.out.println("photo is :"+ photo.toString());
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "Cloudinary"));
            System.out.println("upload result is :"+uploadResult.toString());// optional folder
            String img = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            System.out.println("public_id"+publicId);
            categoryPhotosRepository.save(new CategoryPhotos(publicId,img,false,photo.orElse(null)));
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
            Optional<CategoryPhotos> optionalPhoto = isPhotoOwner(getCurrentUserEmail(), photoId);
            if (optionalPhoto.isPresent()) {
                Map result = cloudinary.uploader().destroy("Cloudinary/"+photoId, ObjectUtils.emptyMap());
                System.out.println("photo id from react " + photoId);
                System.out.println(result);
                optionalPhoto.ifPresent(categoryPhotosRepository::delete);
                return ResponseEntity.ok(result.get("result").toString());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
            }
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }

    @Override
    public ResponseEntity<String> updateCategoryText(Integer categoryId, String title) {
        if (!isCategoryOwner(getCurrentUserEmail(), categoryId)) {
            return ResponseEntity.status(403).body("Unauthorized to update");
        }

        userCategoriesRepository.findById(categoryId).ifPresent(cat -> {
            cat.setName(title);
            userCategoriesRepository.save(cat);
        });

        return ResponseEntity.ok("Updated");
    }

    @Override
    public ResponseEntity<String> updateCategoryLcok(Integer categoryId, boolean lock) {
        if (!isCategoryOwner(getCurrentUserEmail(), categoryId)) {
            return ResponseEntity.status(403).body("Unauthorized to lock/unlock category");
        }

        userCategoriesRepository.findById(categoryId).ifPresent(cat -> {
            cat.setLocked(lock);
            userCategoriesRepository.save(cat);
        });

        return ResponseEntity.ok("Lock updated");
    }

    @Override
    public ResponseEntity<String> updatePhotoLcok(Integer photoId, boolean lock) {
        Optional<CategoryPhotos> photoOpt = categoryPhotosRepository.findById(photoId);
        if (!photoOpt.isPresent()) return ResponseEntity.notFound().build();

        CategoryPhotos photo = photoOpt.get();
        if (!isCategoryOwner(getCurrentUserEmail(), photo.getUserCategory().getCategoryId())) {
            return ResponseEntity.status(403).body("Unauthorized to lock/unlock photo");
        }

        photo.setLocked(lock);
        categoryPhotosRepository.save(photo);

        return ResponseEntity.ok("Photo lock updated");
    }

    @Override
    public ResponseEntity<String> updateUserLock(Integer userId, boolean lock) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setLocked(lock);
            userRepository.save(u);
        });

        return ResponseEntity.ok("User lock updated");
    }

    @Override
    public ResponseEntity<User> getUserById(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) return ResponseEntity.notFound().build();

        User user = userOpt.get();
        if (user.isLocked() && !user.getUserEmail().equals(getCurrentUserEmail())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Map<String, String>> getCategoryrById(int categoryId) {
        Optional<UserCategories> catOpt = userCategoriesRepository.findById(categoryId);
        if (!catOpt.isPresent()) return ResponseEntity.notFound().build();

        UserCategories category = catOpt.get();

        if (category.isLocked() && !category.getUser().getUserEmail().equals(getCurrentUserEmail())) {
            return ResponseEntity.status(403).build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("userName", category.getUser().getUserName());
        response.put("categoryName", category.getName());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Boolean> getCategoryrUser(int categoryId) {
        try {
            return ResponseEntity.ok(isCategoryOwner(getCurrentUserEmail(), categoryId));
        }
        catch (Exception e){
            e.printStackTrace(); // This will show the complete error details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}