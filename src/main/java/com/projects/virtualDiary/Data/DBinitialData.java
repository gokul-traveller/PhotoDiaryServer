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
        String img1 ="https://res.cloudinary.com/dcwjkhu6o/image/upload/v1746534947/Cloudinary/f03yui4evs0ztsagpclp.jpg";
        String pid1 ="public_idCloudinary/f03yui4evs0ztsagpclp";
        String img2 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1746535038/Cloudinary/jxgzltid9dnoshf25rcm.webp";
        String pid2 ="Cloudinary/jxgzltid9dnoshf25rcm";
        String img3 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1746535088/Cloudinary/blpknspotreqgbxfgrwh.png";
        String pid3 ="Cloudinary/blpknspotreqgbxfgrwh";

        List<User> users = new ArrayList<>();
        User user = new User( "Robot", "user1@gmial.com", img1, false,new ArrayList<>());
        users.add(user);
        users.add(new User( "Abhi", "user2@gmial.com", img2, false,new ArrayList<>()));
        users.add(new User( "Gokul", "user3@gmial.com", img3, true,new ArrayList<>()));
        users.add(new User( "Hari", "user4@gmial.com", img3, false,new ArrayList<>()));
        users.add(new User( "Aswin", "user5@gmial.com", img1, false,new ArrayList<>()));
        users.add(new User( "Leo", "user6@gmial.com", img2, false,new ArrayList<>()));
        users.add(new User( "Sam", "user7@gmial.com", img1, true,new ArrayList<>()));
        userRepository.saveAll(users);

        UserCategories category1 = new UserCategories(pid2,"Travel",img2,false,user,new ArrayList<>());
        categoryRepository.save(category1);

        photoRepository.save(new CategoryPhotos(pid1, img1, false,category1));
        photoRepository.save(new CategoryPhotos(pid3, img3, false,category1));
        photoRepository.save(new CategoryPhotos(pid2, img2, false,category1));
    }

}
