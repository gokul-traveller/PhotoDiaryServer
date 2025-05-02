package com.projects.virtualDiary.Repo;


import com.projects.virtualDiary.model.UserCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategories, Integer> {
    List<UserCategories> findByUserId(int userId);
}
