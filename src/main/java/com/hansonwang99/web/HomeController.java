package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.User;
import com.hansonwang99.domain.enums.ArticleType;
import com.hansonwang99.domain.enums.IsDelete;
import com.hansonwang99.repository.UserRepository;
import com.hansonwang99.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value="返回用户文章列表标准视图", notes="返回用户文章列表标准视图")
    @RequestMapping(value="/standard/{type}/{userId}",method = RequestMethod.POST)
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
            } else {
                articles = articleService.getArticles( type, userId, pageable );
            }
        }

        model.addAttribute( "type", type );  // 返回到article/standard页面最底部的隐藏input中做记录用
        model.addAttribute("articles", articles);
        model.addAttribute("userId",getUserId());

        return "article/standard";
    }

    @ApiOperation(value="显示特定articleId文章视图", notes="显示特定articleId文章视图")
    @RequestMapping(value = "/showarticle/{articleId}",method = RequestMethod.POST)
    public String fetchOneArticle( Model model, @PathVariable("articleId") Long articleId ) {

        model.addAttribute("articleId", articleId);
        return "article/article";
    }

    @ApiOperation(value="显示搜索结果视图", notes="显示搜索结果视图")
    @RequestMapping(value="/search/{key}",method = RequestMethod.POST)
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

    @ApiOperation(value="显示用户个人门户视图", notes="显示用户个人门户视图")
    @RequestMapping(value="/user/{userId}/{categoryId}",method = RequestMethod.POST)
    public String userPageShow(Model model,@PathVariable("userId") Long userId,@PathVariable("categoryId") Long categoryId,@RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "size", defaultValue = "15") Integer size) {

        User user = userRepository.findOne( userId ); // 从数据库中取出userId用户的所有信息传到前台去

        model.addAttribute("user",user);
        model.addAttribute("loginUser", getUser() );  // 指的是当前登录的用户

        if( getUserId().longValue() == userId.longValue() ) {
            model.addAttribute("myself", IsDelete.YES.toString());
        } else {
            model.addAttribute("myself",IsDelete.NO.toString());
        }

        return "user";
    }

    @ApiOperation(value="显示用户个人门户usercontent视图", notes="显示用户个人门户usercontent视图")
    @RequestMapping(value="/usercontent/{userId}/{categoryId}",method = RequestMethod.POST)
    public String userContentShow(Model model,@PathVariable("userId") Long userId,@PathVariable("categoryId") Long categoryId,@RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "100") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        List<Article> articleList = null;

        if( 0==categoryId ) {
            articleList = articleService.getArticles( "user", userId, pageable );
        } else {
            articleList = articleService.getArticlesOfCategory( userId, ArticleType.PUBLIC, pageable, categoryId );
        }

        model.addAttribute("articles", articleList );
        model.addAttribute("userId",getUserId());

        return "fragments/usercontent";
    }

}
