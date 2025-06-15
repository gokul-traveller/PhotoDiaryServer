package com.projects.virtualDiary.Repo;


import com.projects.virtualDiary.model.UserCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategories, Integer> {

    @Query("SELECT u FROM UserCategories u WHERE u.user.userId = :userId ORDER BY u.categoryId ASC")
    List<UserCategories> findByUserIdOrdered(int userId);

    UserCategories findByCategoryId(int categoryId);

    Optional<UserCategories> findByPublicId(String publicId);
}
