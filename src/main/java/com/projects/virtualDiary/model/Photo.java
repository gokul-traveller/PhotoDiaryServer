package com.projects.virtualDiary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
public class Photo {
    @Getter
    private int id;
    private String userName;
    private String imageData;
    private boolean isLocked;
    private List<String> accessList;

}
