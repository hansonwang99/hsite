package com.hansonwang99.service.impl;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.enums.ArticleType;
import com.hansonwang99.domain.view.ArticleView;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.ArticleService;
import com.hansonwang99.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
@Service(value = "articleService")
public class ArticleServiceImpl implements ArticleService {

    private Long totalArticleNum = 0l;

    @Autowired
    @Resource
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Article> getArticles(String type, Long userId, Pageable pageable) {

        Page<ArticleView> articleViews = null;

        if ( "my".equals(type) ) {
            articleViews = articleRepository.findArticleByUserId( userId, pageable );
        } else if( "user".equals(type) ) {
            articleViews = articleRepository.findArticleByUserId( userId, pageable );
        } else {
//            Category category = categoryRepository.findOne( Long.parseLong(type) );
//            articleViews = articleRepository.findArticleByCategoryId( category.getId(), pageable );
            articleViews = articleRepository.findArticleByCategoryId( Long.parseLong(type), pageable );
        }

        totalArticleNum = articleViews.getTotalElements();
        return convertArticle( articleViews, userId );
    }

    @Override
    public List<Article> getArticlesOfCategory(Long userId, ArticleType type, Pageable pageable, Long categoryId ) {

        Page<ArticleView> articleViews = null;
        articleViews = articleRepository.findArticleByUserIdAndCategoryIdAndType( userId, type, pageable, categoryId );
        return convertArticle( articleViews, userId );
    }

    @Override
    public Article getOneArticle( Long articleId ) {

        return articleRepository.findById( articleId );
    }

    public List<Article> searchMy( Long userId, String key, Pageable pageable ){

        Page<ArticleView> articleViews = articleRepository.searchMyByKey( userId, "%"+key+"%", pageable );
        return convertArticle( articleViews, userId );
    }

    public List<Article> searchOther( Long userId, String key, Pageable pageable ) {

        Page<ArticleView> articleViews = articleRepository.searchOtherByKey( userId, "%"+key+"%", pageable );
        return convertArticle( articleViews, userId );
    }

    @Override
    public Long getTotalArticleNum() {
        return totalArticleNum;
    }

    private List<Article> convertArticle( Page<ArticleView> articleViews, Long userId ) {

        List<Article> articleList = new ArrayList<Article>();

        for( ArticleView view : articleViews ) {

            Article article = new Article( view );
            article.setCreate_time( DateUtils.getTimeFormatText( article.getPublish_at() ) );
            articleList.add( article );
        }
        
        return articleList;
    }
}
