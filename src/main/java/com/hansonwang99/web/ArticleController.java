package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.ArticleService;
import com.hansonwang99.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/6/25.
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/savaarticle", method = RequestMethod.POST)
    public ResponseData create(Article article) {

        try {

            // 每增加一篇文章就需要增加其对应分类下的文章的计数
            categoryRepository.increaseCountById( article.getCategoryId(), DateUtils.getCurrentTime() );

            article.setPublish_at( DateUtils.getCurrentTime() ); // 当前时间
            article.setUserId( getUserId() );                    // userId目前从内存中获取
            article.setUserName( getUserName() );
            article.setCategoryName( categoryRepository.findById(article.getCategoryId()).getName() );
            article.setProfilePicture( getProfilePicture() );
            articleRepository.save( article );
            return new ResponseData(ExceptionMsg.SUCCESS, "/home");
        } catch (Exception e) {
            logger.error("create article failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }

    }

    @RequestMapping(value = "/fetcharticle/standard/{articleId}")
    public Article fetchOneArticle(@PathVariable("articleId") Long articleId ) {

        Article article = articleService.getOneArticle( articleId );
        return article;
    }

    @RequestMapping(value="/delete/{id}")
    public Response delete(@PathVariable("id") Long id) {

        Article article = articleRepository.findOne( id );
        if( null!=article && getUserId().equals(article.getUserId()) ) {

            articleRepository.deleteById( id );
            if( null != article.getCategoryId() ) {
                categoryRepository.reduceCountById( article.getCategoryId(), DateUtils.getCurrentTime() );
            }
        }

        return result();
    }

}
