package com.projects.virtualDiary.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
public class UserCategories {

    private String id;

    private String name;

    private String imageData;

    private boolean isLocked;

    public String getPhotoId() {
        return id;
    }
}
