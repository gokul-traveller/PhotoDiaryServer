package com.projects.virtualDiary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPhotosDTO {

    private int photoId;
    private String publicId;
    private String imageData;
    private boolean locked;
    private int userCategoryId;

}
