package com.projects.virtualDiary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String userName;
    private String email;
    private String imageData;
    private boolean isLocked;
}
