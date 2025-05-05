package com.projects.virtualDiary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class UserCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    private String publicId;

    private String name;

    private String imageData;

    private boolean isLocked;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "userCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CategoryPhotos> userCategoryPhotos;

    public UserCategories(String publicId, String name, String imageData, boolean isLocked, User user, List<CategoryPhotos> userCategoryPhotos) {
        this.publicId = publicId;
        this.name = name;
        this.imageData = imageData;
        this.isLocked = isLocked;
        this.user = user;
        this.userCategoryPhotos = userCategoryPhotos;
    }
}
