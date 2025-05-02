package com.projects.virtualDiary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPhotos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int photoId;

    private String publicId;

    private String imageData;

    private boolean locked;


    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private UserCategories categories;

}
