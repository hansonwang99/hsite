package com.hansonwang99.repository;

import com.hansonwang99.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById( Long categoryId );

    Category findByUserIdAndName( Long userId, String name );

    List<Category> findByUserIdOrderByIdDesc(Long userId);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Category c set c.count=(c.count+1),c.lastModifyTime =:lastModifyTime where c.id =:id")
    void increaseCountById(@Param("id") Long id, @Param("lastModifyTime") Long lastModifyTime);


}
