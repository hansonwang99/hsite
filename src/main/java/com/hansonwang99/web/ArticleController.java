package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.enums.ArticleType;
import com.hansonwang99.domain.Category;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.ArticleService;
import com.hansonwang99.service.CategoryService;
import com.hansonwang99.utils.DateUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @ApiOperation(value="保存文章 RC", notes="将用户撰写的文章存到数据库")
    @RequestMapping(value = "/savaarticle", method = RequestMethod.POST)
    public ResponseData create(Article article) {

        try {

            if( ""==article.getTag() ) {
                article.setTag("默认标签");
            }

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

    @ApiOperation(value="取一篇文章 RC", notes="以Standard视图从数据库中取一篇文章")
    @RequestMapping(value = "/fetcharticle/standard/{articleId}", method = RequestMethod.POST)
    public Article fetchOneArticle(@PathVariable("articleId") Long articleId ) {

        Article article = articleService.getOneArticle( articleId );
        return article;
    }

    @ApiOperation(value="删一篇文章 RC", notes="从数据库中删除一篇文章")
    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST)
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
    @ApiOperation(value="获取文章属性（小弹窗修改文章属性时） RC", notes="获取文章属性（小弹窗修改文章属性时）")
    @RequestMapping(value="/detail/{id}", method = RequestMethod.POST)
    public Article detail(@PathVariable("id") long id) {
        Article article = articleRepository.findOne(id);
        return article;
    }

    @ApiOperation(value="提交修改文章属性（小弹窗修改文章属性，点击“提交”按钮时） RC", notes="提交修改文章属性（小弹窗修改文章属性，点击“提交”按钮时）")
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

    @ApiOperation(value="获取文章列表 RC", notes="获取文章列表")
    @RequestMapping(value="/standard/{type}/{userId}", method = RequestMethod.POST)
    public List<Article> standard( @RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "15") Integer size, @PathVariable("type") String type, @PathVariable("userId") Long userId ) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        List<Article> articles = null;

        if ( "my".equals(type) ) {

            if ( null!=userId && 0==userId ) {
                articles = articleService.getArticles( type, getUserId(), pageable );
            }
        } else {

            if ( null!=userId && 0==userId ) {
                articles = articleService.getArticles( type, getUserId(), pageable );
            } else {
                articles = articleService.getArticles( type, userId, pageable );
            }
        }

        return articles;
    }

    @ApiOperation(value="搜索本人文章并返回列表 RC", notes="搜索本人文章并返回列表")
    @RequestMapping(value="/searchMy/{key}", method = RequestMethod.POST)
    public List<Article> searchMy( @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "20") Integer size, @PathVariable("key") String key) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        List<Article> myArticles = articleService.searchMy( getUserId(), key , pageable );
        return myArticles;
    }

    @ApiOperation(value="搜索其他人文章并返回列表 RC", notes="搜索其他人文章并返回列表")
    @RequestMapping(value="/searchOther/{key}", method = RequestMethod.POST)
    public List<Article> searchOther( @RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "20") Integer size, @PathVariable("key") String key) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        List<Article> otherArticles = articleService.searchOther( getUserId(), key, pageable );
        return otherArticles;
    }

    @ApiOperation(value="设置文章私密与否 RC", notes="设置文章私密与否")
    @RequestMapping(value="/changePrivacy/{id}/{type}", method = RequestMethod.POST)
    public Response changePrivacy(@PathVariable("id") long id,@PathVariable("type") ArticleType type) {

        articleRepository.modifyArticleTypeByIdAndUserId( type, id, getUserId() );
        return result();
    }


}
