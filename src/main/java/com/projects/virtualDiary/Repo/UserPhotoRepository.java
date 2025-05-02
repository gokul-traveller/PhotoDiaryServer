package com.projects.virtualDiary.Repo;

import com.projects.virtualDiary.model.CategoryPhotos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhotoRepository extends JpaRepository<CategoryPhotos, Integer> {
    List<CategoryPhotos> findByCategoryId(int categoryId);
}

