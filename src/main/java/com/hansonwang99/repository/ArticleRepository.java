package com.hansonwang99.repository;

import com.hansonwang99.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/6/24.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

    public String baseSql="select c.id as id, c.title as title, c.publish_at as publish_at, c.content as content, c.tag as tag";

}
