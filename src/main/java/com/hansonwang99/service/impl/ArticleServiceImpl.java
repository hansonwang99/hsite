package com.hansonwang99.service.impl;

import com.hansonwang99.domain.Article;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
@Service(value = "articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    @Resource
    private ArticleRepository articleRepository;

    @Override
    public List<Article> getArticles(String type, Long userId, Pageable pageable) {

        Page<Article> articles = null;
        articles = articleRepository.findArticleByUserId( userId, pageable );
        return convertArticle( articles, userId );
    }

    private List<Article> convertArticle( Page<Article> articles, Long userId ) {

        List<Article> articleList = new ArrayList<Article>();

        for( Article article : articles ) {
            articleList.add( article );
        }
        
        return articleList;
    }
}
