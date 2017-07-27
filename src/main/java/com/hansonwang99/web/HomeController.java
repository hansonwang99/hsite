package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value="/standard/{type}/{userId}")
    public String standard( Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "15") Integer size, @PathVariable("type") String type, @PathVariable("userId") Long userId) {

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
            }
        }

        model.addAttribute( "type", type );  // 返回到article/standard页面最底部的隐藏input中做记录用
        model.addAttribute("articles", articles);
        model.addAttribute("userId",getUserId());

        return "article/standard";
    }

    @RequestMapping(value = "/showarticle/{articleId}")
    public String fetchOneArticle( Model model, @PathVariable("articleId") Long articleId ) {

        model.addAttribute("articleId", articleId);
        return "article/article";
    }

    @RequestMapping(value="/search/{key}")
    public String search(Model model,@RequestParam(value = "page", defaultValue = "0") Integer page,
                         @RequestParam(value = "size", defaultValue = "20") Integer size, @PathVariable("key") String key) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        List<Article> myArticles = articleService.searchMy( getUserId(), key , pageable );
        List<Article> otherArticles = articleService.searchOther( getUserId(), key, pageable );

        model.addAttribute("myArticles", myArticles);
        model.addAttribute("otherArticles", otherArticles);
        model.addAttribute("userId", getUserId());
        model.addAttribute("mysize", myArticles.size());
        model.addAttribute("othersize", otherArticles.size());
        model.addAttribute("key", key);

        return "article/search";
    }

    @RequestMapping(value="/user/{userId}/{categoryId}")
    public String userPageShow(Model model,@PathVariable("userId") Long userId,@PathVariable("categoryId") Long categoryId,@RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "size", defaultValue = "15") Integer size) {


        return "user";
    }

}
