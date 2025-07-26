package com.projects.virtualDiary.Repo;


import com.projects.virtualDiary.model.UserCategories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategories, Integer> {

    @Query("SELECT u FROM UserCategories u WHERE u.user.userId = :userId ORDER BY u.categoryId ASC")
    List<UserCategories> findByUserIdOrdered(int userId);

    List<UserCategories> findByUserUserIdOrderByCategoryIdAsc(int userId);

    // Optional wrapper for null safety
    Optional<UserCategories> findByCategoryId(int categoryId);

    // Retrieve by public ID (ensure this field is unique or indexed in DB)
    Optional<UserCategories> findByPublicId(String publicId);

    // Paginated version of fetching categories for a user
    Page<UserCategories> findByUserUserIdOrderByCategoryIdAsc(int userId, Pageable pageable);

    // Fetch unlocked (public) categories for a user
    List<UserCategories> findAllByUserUserIdAndIsLockedFalse(int userId);
}
