package com.hansonwang99.web;

import com.hansonwang99.comm.Const;
import com.hansonwang99.domain.User;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
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

}
