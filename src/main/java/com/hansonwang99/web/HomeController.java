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

        System.out.println( "jinru" );

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        List<Article> articles = null;
        if ( "my".equals(type) ) {
            if ( null!=userId && 0==userId ) {
                articles = articleService.getArticles( type, getUserId(), pageable );
            }
        }

        model.addAttribute("articles", articles);

        System.out.println( articles.size() );

//        return "article/standard";
        model.addAttribute("user",getUser());
        return "home";
    }
}
