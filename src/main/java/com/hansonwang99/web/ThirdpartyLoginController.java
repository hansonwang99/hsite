package com.hansonwang99.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hansonwang99.comm.Const;
import com.hansonwang99.domain.Category;
import com.hansonwang99.domain.User;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.repository.UserRepository;
import com.hansonwang99.service.CategoryService;
import com.hansonwang99.utils.DateUtils;
import com.hansonwang99.utils.HttpRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/9/19.
 */
@Controller
@RequestMapping("/")
public class ThirdpartyLoginController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Resource
    private CategoryService categoryService;

    private int count = 0;
    private String access_token = "";
    private String finaRes = "";

    @ApiOperation(value="github登录", notes="github登录")
    @RequestMapping(value="/githublogin", method = RequestMethod.GET)
    public String githubLogin(@RequestParam(value = "code") String code, HttpServletResponse response, Model model ) {

        count++;

        String client_id = "5f4e0e7158ef656b3575";
        String client_secret = "5160e0bbd4b2236a5fd228660d5ecf711fb60af5";
        String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=" +client_id+ "&client_secret=" +client_secret+ "&code=" +code;


        if( count<2 ) {

            String res = HttpRequest.sendPost(ACCESS_TOKEN_URL,null);
            access_token = res.split("&")[0];

            System.out.println("111");

            String USERINFO_URL = "https://api.github.com/user?"+access_token;
            finaRes = HttpRequest.sendGet(USERINFO_URL);
            System.out.println(finaRes);
            System.out.println("222");
        } else
            count = 0;

        JSONObject githubUserInfo = (JSONObject) JSON.parse(finaRes);

        System.out.println("333");

        String userName  = githubUserInfo.getString("login");
        String userEmail = githubUserInfo.getString("email");
        User loginUser = userRepository.findByUserName(userName);

        System.out.println("444");

        if (loginUser == null ) { // 用户不存在，则应该将用户存到数据库中
            loginUser = new User();
            loginUser.setUserName( userName );
            if( null==userEmail )
                userEmail = "null@" + userName + "null.com";
            loginUser.setEmail( userEmail );
            loginUser.setPassWord( "nullpassword" );  // 通过第三方登录的用户是没有密码的，给个默认值
            loginUser.setCreateTime(DateUtils.getCurrentTime());
            loginUser.setLastModifyTime(DateUtils.getCurrentTime());
            loginUser.setProfilePicture( githubUserInfo.getString("avatar_url") );
            userRepository.save(loginUser);

            System.out.println("555");
        }

        loginUser = userRepository.findByUserName(userName);


        System.out.println("666");

        Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, cookieSign(loginUser.getId().toString()));
        cookie.setMaxAge(Const.COOKIE_TIMEOUT);
        cookie.setPath("/");
        response.addCookie(cookie);
        getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);

        System.out.println("777");

        String defaultCategoryName = "默认分类";
        Category category = categoryRepository.findByUserIdAndName( getUserId(), defaultCategoryName );
        if(null != category){
            logger.info("默认分类已经存在了，无需再创建");
        }else{
            try {
                categoryService.saveCategory(getUserId(), 0l, defaultCategoryName);
            } catch (Exception e) {
                logger.error("创建默认分类出现异常：",e);
            }
        }

        System.out.println("888");

        model.addAttribute("user",getUser());

        return "home";
    }
}