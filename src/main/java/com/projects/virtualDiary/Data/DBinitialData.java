package com.projects.virtualDiary.Data;

import com.projects.virtualDiary.model.CategoryPhotos;
import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.model.UserCategories;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class DBinitialData {
    DBinitialData(){
        initialize();
    }

    List<User> users = new ArrayList<>();
    List<UserCategories> categories= new ArrayList<>();
    List<CategoryPhotos> photos= new ArrayList<>();

    private void initialize() {
        String img1 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284939/Cloudinary/a5nkdy9xj71rrqix7qxt.jpg";
        String img2 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744284419/Cloudinary/wpmke9rpoxjaiqxqnzuv.png";
        String img3 = "https://res.cloudinary.com/dcwjkhu6o/image/upload/v1744285018/Cloudinary/wcos1bztuuizzfngsqrv.jpg";

        User user = new User(1, "user1", "user1@gmial.com", img1, false,null);
        users.add(user);
        users.add(new User(2, "user2", "user2@gmial.com", img2, false,null));
        users.add(new User(3, "user3", "user3@gmial.com", img3, true,null));
        users.add(new User(4, "user4", "user4@gmial.com", img3, false,null));
        users.add(new User(5, "user5", "user5@gmial.com", img1, false,null));
        users.add(new User(6, "user6", "user6@gmial.com", img2, false,null));
        users.add(new User(7, "user7", "user7@gmial.com", img1, true,null));

        UserCategories category1 = new UserCategories(1,"PublicID","Travel",img3,false,user,null);
        categories.add(category1);

        photos.add(new CategoryPhotos(1,"publicId", img1, false,category1));
        photos.add(new CategoryPhotos(1,"publicId", img3, false,category1));
        photos.add(new CategoryPhotos(1,"publicId", img2, false,category1));
    }

}
