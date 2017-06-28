package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/6/25.
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping(value = "/savaarticle", method = RequestMethod.POST)
    public ResponseData create(Article article) {

        try {
            article.setPublish_at( DateUtils.getCurrentTime() ); // 当前时间
            article.setUserId( getUserId() );                    // userId目前从内存中获取
            article.setCategoryId( 0l );                         // 目前所有categoryId都是0
            articleRepository.save( article );
            return new ResponseData(ExceptionMsg.SUCCESS, "/home");
        } catch (Exception e) {
            logger.error("create article failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }

    }
}
