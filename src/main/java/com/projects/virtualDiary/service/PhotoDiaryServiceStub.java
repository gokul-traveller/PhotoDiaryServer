package com.projects.virtualDiary.service;

import com.projects.virtualDiary.model.Photo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.util.Base64;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PhotoDiaryServiceStub implements PhotoDiaryService{
    @Override
    public List<Photo> getAllUsers() {
        List<Photo> users = new ArrayList<>();
        ClassPathResource imgFile = new ClassPathResource("images/abstractWallpaper.jpg");
        // Convert image bytes to Base64
        try {
            byte[] bytes = Files.readAllBytes(imgFile.getFile().toPath());
            String image = Base64.getEncoder().encodeToString(bytes);
            users.add(new Photo(1, "user1", image, true, Collections.emptyList()));
            users.add(new Photo(2, "user2", image, true, Collections.emptyList()));
            users.add(new Photo(3, "user3", image, true, Collections.emptyList()));
            users.add(new Photo(4, "user4", image, true, Collections.emptyList()));
            users.add(new Photo(5, "user5", image, false, Collections.emptyList()));
            users.add(new Photo(6, "user6", image, false, Collections.emptyList()));
            users.add(new Photo(7, "user7", image, true, Collections.emptyList()));
            users.add(new Photo(8, "user8", image, false, Collections.emptyList()));
            users.add(new Photo(9, "user9", image, true, Collections.emptyList()));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return users;
    }
}
