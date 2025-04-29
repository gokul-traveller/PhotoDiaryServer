package com.projects.virtualDiary.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CategoryPhotos {
    private int photoId;
    private String userName;
    private List<Photos> photos;

}
