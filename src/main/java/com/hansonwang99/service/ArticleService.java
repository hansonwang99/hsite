package com.hansonwang99.service;

import com.hansonwang99.domain.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
public interface ArticleService {

    public List<Article> getArticles( String type, Long userId, Pageable pageable );

    public Article getOneArticle( Long articleId );
}
