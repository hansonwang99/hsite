package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.Category;
import com.hansonwang99.domain.result.BackadminResult;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.ResponseData;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.ArticleService;
import com.hansonwang99.utils.DateUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
@RestController
@RequestMapping("/backadmin")
public class BackadminController extends BaseController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @ApiOperation(value="获取文章列表(后台管理) RC", notes="获取文章列表(后台管理)")
    @RequestMapping(value="/articlelist/{type}/{userId}", method = RequestMethod.GET)
    public BackadminResult backadminArticleList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size, @PathVariable("type") String type, @PathVariable("userId") Long userId ) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page-1, size, sort);

        List<Article> articles = articleService.getArticles( type, getUserId(), pageable );

        BackadminResult backadminResult = new BackadminResult();
        backadminResult.setPage( page );
        backadminResult.setPageSize( size );
        backadminResult.setPageTotalNum( articleService.getTotalArticleNum() );
        backadminResult.setArticles( articles );

        return backadminResult;
    }

    @ApiOperation(value="获取特定ID的文章(后台管理) RC", notes="获取特定ID的文章(后台管理)")
    @RequestMapping(value="/article", method = RequestMethod.GET)
    public Article backadminOneArticle(@RequestParam(value = "id") Long id) {
        Article article = articleService.getOneArticle( id );
        return article;
    }

    @ApiOperation(value="获取所有文章分类（后台管理） RC", notes="获取所有文章分类（后台管理）")
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public List<Category> getCategories() {
        List<Category> categories = null;
        try {
            Long id = getUserId();
            categories = categoryRepository.findByUserIdOrderByIdDesc(id);
        } catch (Exception e) {
            logger.error("getCategory failed, ", e);
        }
        return categories;
    }

    @ApiOperation(value="修改文章（后台管理） RC", notes="修改文章（后台管理）")
    @RequestMapping(value = "/modifyarticle", method = RequestMethod.POST)
    public ResponseData modifyArticle( Article article ) {

        try {
            Long articleId = article.getId();
            String articleTitle = article.getTitle();
            String articleTag = article.getTag();
            Long articleCategoryId = article.getCategoryId();
            String articleCategoryName = article.getCategoryName();
            String articleContent = article.getContent();

//            if( ""==articleTag )
//                articleTag = "默认标签";
//            if( ""==articleTitle )
//                articleTitle = "默认标题";
//            if( ""==articleContent )
//                articleContent = "您文章内容为空！";

            Category categoryOld = categoryRepository.findById( articleRepository.findById(articleId).getCategoryId() );
            if( categoryOld.getId() != articleCategoryId ) { // 说明用户从下拉列表里选了别的分类，此时要更新新老分类下的计数
                categoryRepository.increaseCountById( articleCategoryId, DateUtils.getCurrentTime() );
                categoryRepository.reduceCountById( categoryOld.getId(), DateUtils.getCurrentTime() );
            }

            articleRepository.updateArticleById( articleId, articleTitle, articleTag, articleCategoryId, articleCategoryName, articleContent );
        }catch (Exception e) {
            logger.error("modify article property failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }

        return new ResponseData(ExceptionMsg.SUCCESS);
    }

    @ApiOperation(value="删除特定ID的文章(后台管理) RC", notes="删除特定ID的文章(后台管理)")
    @RequestMapping(value="/delete", method = RequestMethod.GET)
    public ResponseData backadminDelArticleById(@RequestParam(value = "id") Long id) {

        try {
            Article article = articleService.getOneArticle( id );
            if( null!=article && getUserId().equals(article.getUserId()) ) {

                articleRepository.deleteById( id );
                if( null != article.getCategoryId() ) {
                    categoryRepository.reduceCountById( article.getCategoryId(), DateUtils.getCurrentTime() );
                }
            }
        }catch (Exception e) {
            logger.error("delete article failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }

        return new ResponseData(ExceptionMsg.SUCCESS);
    }
}
