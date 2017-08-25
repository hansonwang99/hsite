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

    @Query(baseSql+ " where a.userId=?1 and a.categoryId=?2 ")
    Page<ArticleView> findArticleByUserIdAndCategoryId(Long userId, Pageable pageable, Long categoryId);

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

    @Query(baseSql+ " where a.userId=?1 and ( a.title like ?2) ")
    Page<ArticleView> searchMyByKey(Long userId,String key,Pageable pageable);

    @Query(baseSql+ " where a.userId!=?1 and ( a.title like ?2) ")
    Page<ArticleView> searchOtherByKey(Long userId,String key,Pageable pageable);

    @Transactional
    Long deleteById(Long id);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Article a set a.title =:title,a.tag =:tag,a.categoryId =:categoryId,a.categoryName =:categoryName where a.id =:id")
    void updateArticlePropertyById(@Param("id") Long id, @Param("title") String title, @Param("tag") String tag, @Param("categoryId") Long categoryId, @Param("categoryName") String categoryName );

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Article a set a.title =:title,a.tag =:tag,a.categoryId =:categoryId,a.categoryName =:categoryName,a.content =:content where a.id =:id")
    void updateArticleById(@Param("id") Long id, @Param("title") String title, @Param("tag") String tag, @Param("categoryId") Long categoryId, @Param("categoryName") String categoryName, @Param("content") String content );
}
