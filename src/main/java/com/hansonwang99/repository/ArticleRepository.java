package com.hansonwang99.repository;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.view.ArticleView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2017/6/24.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

    public String baseSql="select a.id as id, a.userId as userId, a.categoryId as categoryId, a.title as title, a.publish_at as publish_at, a.content as content, a.tag as tag "
            + "from Article a";

    @Query(baseSql+ " where a.userId=?1 ")
    Page<ArticleView> findArticleByUserId(Long userId, Pageable pageable);

    Article findById( Long articleId );
}
