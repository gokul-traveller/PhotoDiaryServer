package com.projects.virtualDiary.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Photos {
    private String photoId;
    private String imageData;
    private boolean locked;
}
