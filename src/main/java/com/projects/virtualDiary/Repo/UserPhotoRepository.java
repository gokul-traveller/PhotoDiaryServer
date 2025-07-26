package com.projects.virtualDiary.Repo;

import com.projects.virtualDiary.model.CategoryPhotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPhotoRepository extends JpaRepository<CategoryPhotos, Integer> {

    // Fetch all photos for a given category
    List<CategoryPhotos> findByUserCategory_CategoryId(int categoryId);

    // Find photo by its public identifier (ensure uniqueness in DB)
    Optional<CategoryPhotos> findByPublicId(String publicId);

    // Fetch only unlocked (public) photos in a category
    List<CategoryPhotos> findByUserCategory_CategoryIdAndLockedFalse(int categoryId);

    // Optional: Fetch all photos across multiple categories
    List<CategoryPhotos> findByUserCategory_CategoryIdIn(List<Integer> categoryIds);
}

