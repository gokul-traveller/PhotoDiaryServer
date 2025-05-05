package com.projects.virtualDiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
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
    @JsonIgnore
    private UserCategories userCategory;

    public CategoryPhotos(String publicId, String img3, boolean locked, UserCategories category1) {
        this.publicId = publicId;
        this.imageData = img3;
        this.locked = locked;
        this.userCategory = category1;
    }
}
