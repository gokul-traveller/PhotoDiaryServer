package com.projects.virtualDiary.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Photos {
    private Integer photoId;
    private String publicId;
    private String imageData;
    private boolean locked;
}
