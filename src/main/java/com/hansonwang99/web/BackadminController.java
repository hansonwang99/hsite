package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.result.BackadminResult;
import com.hansonwang99.service.ArticleService;
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
    private ArticleService articleService;

    @ApiOperation(value="获取文章列表(后台管理) RC", notes="获取文章列表(后台管理)")
    @RequestMapping(value="/articlelist/{type}/{userId}", method = RequestMethod.GET)
    public BackadminResult backadminArticleList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size, @PathVariable("type") String type, @PathVariable("userId") Long userId ) {

        Sort sort = new Sort(Sort.Direction.ASC, "id");
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
}
