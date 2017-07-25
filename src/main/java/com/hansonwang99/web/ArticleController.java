package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.Category;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.ArticleService;
import com.hansonwang99.service.CategoryService;
import com.hansonwang99.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @Resource
    private CategoryService categoryService;

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

    /**
     * 修改文章属性
     * @param id
     * @return
     */
    @RequestMapping(value="/detail/{id}")
    public Article detail(@PathVariable("id") long id) {
        Article article = articleRepository.findOne(id);
        return article;
    }

    @RequestMapping(value = "/modifyarticleprop", method = RequestMethod.POST)
    public ResponseData modifyArticleProperty( Article article ) {

        ResponseData responseData = new ResponseData( ExceptionMsg.SUCCESS, "" );
        Long articleId = article.getId();
        String articleTitle = article.getTitle();
        String articleTag = article.getTag();
        Long articleCategoryId = 0l;
        String articleCategoryName = "";

        try {

            Category categoryOld = categoryRepository.findById( article.getCategoryId() );
            String categoryNameTmp = article.getCategoryName();
            if( "" != categoryNameTmp ) {  // 如果不为空，说明用户输入了一个新的文章分类名
               Category categoryNew = categoryRepository.findByUserIdAndName( getUserId(), categoryNameTmp );
               if( null != categoryNew ) {  // 该分类已经存在
                    categoryRepository.increaseCountById( categoryNew.getId(), DateUtils.getCurrentTime() );
                    categoryRepository.reduceCountById( categoryOld.getId(), DateUtils.getCurrentTime() );

                    articleCategoryId = categoryNew.getId();
                    articleCategoryName = categoryNew.getName();

                    responseData.setRspCode( ExceptionMsg.CollectExist.getCode() );
                    responseData.setRspMsg( ExceptionMsg.CollectExist.getMsg() );
               } else {                    // 该分类不存在，需要新建分类
                    Category category = categoryService.saveCategory( getUserId(), 1l, categoryNameTmp );

                    articleCategoryId = category.getId();
                    articleCategoryName = categoryNameTmp;
               }

            } else { // 如果为空，说明用户没输新的分类名，此时用户可能在下拉列表中选择了新的分类，也有可能根本没动

                Category categoryOldTmp = categoryRepository.findById( articleRepository.findById(articleId).getCategoryId() );

                if( categoryOldTmp.getId() != article.getCategoryId() ) { // 说明用户从下拉列表里选了已有的别的分类
                    categoryRepository.increaseCountById( article.getCategoryId(), DateUtils.getCurrentTime() );
                    categoryRepository.reduceCountById( categoryOldTmp.getId(), DateUtils.getCurrentTime() );
                }

                articleCategoryId = categoryOld.getId();
                articleCategoryName = categoryOld.getName();
            }

            // 去数据库中去更新当前文章的属性（包括：标题、标签、分类ID、分类名）
            articleRepository.updateArticlePropertyById( articleId, articleTitle, articleTag, articleCategoryId, articleCategoryName );
            responseData.setData( articleCategoryName );
            return responseData;
        } catch (Exception e) {
            logger.error("modify article property failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

}
