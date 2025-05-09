package com.projects.virtualDiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String userName;

    private String email;

    private String imageData;

    private boolean isLocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<UserCategories> userCategories;

    public User( String userName, String email, String imageData, boolean isLocked, List<UserCategories> userCategories) {
        this.userName = userName;
        this.email = email;
        this.imageData = imageData;
        this.isLocked = isLocked;
        this.userCategories = userCategories;
    }
}
