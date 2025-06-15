package com.projects.virtualDiary.Repo;

import com.projects.virtualDiary.model.CategoryPhotos;
import java.util.List;
import java.util.Optional;

import com.projects.virtualDiary.model.UserCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhotoRepository extends JpaRepository<CategoryPhotos, Integer> {
    List<CategoryPhotos> findByUserCategory_CategoryId(int categoryId);

    Optional<CategoryPhotos> findByPublicId(String photoId);

}

