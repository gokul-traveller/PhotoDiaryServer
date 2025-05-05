package com.projects.virtualDiary.Data;

import com.projects.virtualDiary.Repo.UserCategoryRepository;
import com.projects.virtualDiary.Repo.UserPhotoRepository;
import com.projects.virtualDiary.Repo.UserRepository;
import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DBinitialData {

    private final UserRepository userRepository;
    private final UserCategoryRepository categoryRepository;
    private final UserPhotoRepository photoRepository;

    @PostConstruct
    public void initialize() {
        if (userRepository.count() > 0) {
            System.out.println("Initial data already exists. Skipping seeding.");
            return;
        }

        seedData();
    }

    private void seedData() {
        String img1 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284939/Cloudinary/a5nkdy9xj71rrqix7qxt.jpg";
        String img2 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284419/Cloudinary/wpmke9rpoxjaiqxqnzuv.png";
        String img3 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744285018/Cloudinary/wcos1bztuuizzfngsqrv.jpg";

        List<User> users = new ArrayList<>();
        User user = new User( "Robot", "user1@gmial.com", img1, false,null);
        users.add(user);
        users.add(new User( "Abhi", "user2@gmial.com", img2, false,null));
        users.add(new User( "Gokul", "user3@gmial.com", img3, true,null));
        users.add(new User( "Hari", "user4@gmial.com", img3, false,null));
        users.add(new User( "Aswin", "user5@gmial.com", img1, false,null));
        users.add(new User( "Leo", "user6@gmial.com", img2, false,null));
        users.add(new User( "Sam", "user7@gmial.com", img1, true,null));
        userRepository.saveAll(users);

        UserCategories category1 = new UserCategories("PublicID","Travel",img3,false,user,null);
        categoryRepository.save(category1);

        photoRepository.save(new CategoryPhotos("publicId", img1, false,category1));
        photoRepository.save(new CategoryPhotos("publicId", img3, false,category1));
        photoRepository.save(new CategoryPhotos("publicId", img2, false,category1));
    }

}
