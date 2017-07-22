package com.hansonwang99.repository;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.view.ArticleView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2017/6/24.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

    public String baseSql="select a.id as id, a.userId as userId, a.userName as userName, a.categoryId as categoryId, a.categoryName as categoryName, a.title as title, a.publish_at as publish_at, a.create_time as create_time, a.content as content, a.tag as tag, a.profilePicture as profilePicture "
            + "from Article a";

    @Query(baseSql+ " where a.userId=?1 ")
    Page<ArticleView> findArticleByUserId(Long userId, Pageable pageable);

    @Query(baseSql+ " where a.categoryId=?1 ")
    Page<ArticleView> findArticleByCategoryId(Long categoryId, Pageable pageable);

    Article findById( Long articleId );

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Article set userName=:userName where userId=:userId")
    int setArticleUserName(@Param("userName") String userName, @Param("userId") Long userId);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Article set profilePicture=:profilePicture where userId=:userId")
    int setArticleProfilePicture(@Param("profilePicture") String profilePicture, @Param("userId") Long userId);
}
