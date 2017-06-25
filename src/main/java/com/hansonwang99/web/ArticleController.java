package com.hansonwang99.web;

import com.hansonwang99.domain.Article;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.repository.ArticleRepository;
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
    public Response create(Article article) {
        try {

            System.out.println(article.getContent());
            articleRepository.save( article );
        } catch (Exception e) {

            logger.error("create user failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }
}
