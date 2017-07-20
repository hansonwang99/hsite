package com.hansonwang99.web;

import com.hansonwang99.comm.Const;
import com.hansonwang99.domain.User;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
import com.hansonwang99.repository.ArticleRepository;
import com.hansonwang99.repository.UserRepository;
import com.hansonwang99.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hansonwang on 2017/6/18.
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseData login(User user,HttpServletResponse response) {
        try {

            User loginUser = userRepository.findByUserNameOrEmail(user.getUserName(), user.getEmail());
            if (loginUser == null ) {
                return new ResponseData(ExceptionMsg.LoginNameNotExists);
            }else if(!loginUser.getPassWord().equals(getPwd(user.getPassWord()))){
                return new ResponseData(ExceptionMsg.LoginNameOrPassWordError);
            }
            Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, cookieSign(loginUser.getId().toString()));
            cookie.setMaxAge(Const.COOKIE_TIMEOUT);
            cookie.setPath("/");
            response.addCookie(cookie);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
            String preUrl = "/home";
            return new ResponseData(ExceptionMsg.SUCCESS, preUrl);

        } catch (Exception e) {

            logger.error("login failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public Response create(User user) {
        try {
            User registUser = userRepository.findByEmail(user.getEmail());
            if (null != registUser) {
                return result(ExceptionMsg.EmailUsed);
            }
            User userNameUser = userRepository.findByUserName(user.getUserName());
            if (null != userNameUser) {
                return result(ExceptionMsg.UserNameUsed);
            }
            user.setPassWord(getPwd(user.getPassWord()));
            user.setCreateTime(DateUtils.getCurrentTime());
            user.setLastModifyTime(DateUtils.getCurrentTime());
            user.setProfilePicture("img/favicon.png");
            userRepository.save(user);

            getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);

        } catch (Exception e) {

            logger.error("create user failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    /**
     * 修改个人简介
     * @param introduction
     * @return
     */
    @RequestMapping(value = "/updateIntroduction", method = RequestMethod.POST)
    public ResponseData updateIntroduction(String introduction) {
        try {
            User user = getUser();
            userRepository.setIntroduction(introduction, user.getEmail());
            user.setIntroduction(introduction);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
            return new ResponseData(ExceptionMsg.SUCCESS, introduction);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("updateIntroduction failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 修改昵称
     * @param userName
     * @return
     */
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    public ResponseData updateUserName(String userName) {
        try {
            User loginUser = getUser();
            if(userName.equals(loginUser.getUserName())){
                return new ResponseData(ExceptionMsg.UserNameSame);
            }
            User user = userRepository.findByUserName(userName);
            if(null != user && user.getUserName().equals(userName)){
                return new ResponseData(ExceptionMsg.UserNameUsed);
            }
            if(userName.length()>12){
                return new ResponseData(ExceptionMsg.UserNameLengthLimit);
            }
            userRepository.setUserName(userName, loginUser.getEmail());
            loginUser.setUserName(userName);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
            articleRepository.setArticleUserName( userName, getUserId() ); // 用户昵称一旦修改，所有该用户名下的所有文章的用户名字段也得更新
            return new ResponseData(ExceptionMsg.SUCCESS, userName);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("updateUserName failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

}
