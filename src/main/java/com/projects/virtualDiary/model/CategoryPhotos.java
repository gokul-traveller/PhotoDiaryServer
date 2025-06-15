package com.projects.virtualDiary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

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
    @JsonBackReference  // Prevents infinite recursion during JSON serialization
    @ToString.Exclude   // Avoid stack overflow in Lombok toString
    private UserCategories userCategory;

    public CategoryPhotos(String publicId, String imageData, boolean locked, UserCategories userCategory) {
        this.publicId = publicId;
        this.imageData = imageData;
        this.locked = locked;
        this.userCategory = userCategory;
    }
}
