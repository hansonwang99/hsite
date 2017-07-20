package com.hansonwang99.web;

import com.hansonwang99.comm.Const;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hansonwang on 2017/6/2.
 */
@Controller
public class IndexController extends BaseController {

    @RequestMapping(value="/index",method= RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value="/layout",method= RequestMethod.GET)
    public String layout(Model model) {
        return "layout";
    }

    @RequestMapping(value="/collector",method= RequestMethod.GET)
    public String collector(Model model) {
        return "collector";
    }

    @RequestMapping(value="/home",method= RequestMethod.GET)
    public String home(Model model) {

        model.addAttribute("user",getUser());
        System.out.println(getUser().getUserName());
        return "home";
    }

    @RequestMapping(value="/main",method= RequestMethod.GET)
    public String main(Model model) {
        return "main";
    }

    @RequestMapping(value="/post",method= RequestMethod.GET)
    public String post(Model model) {
        return "post";
    }

    @RequestMapping(value="/write",method= RequestMethod.GET)
    public String write(Model model) {
        return "write";
    }

    @RequestMapping(value="/register",method= RequestMethod.GET)
    public String register(Model model) {
        return "register";
    }

    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public String logout(HttpServletResponse response, Model model) {
        getSession().removeAttribute(Const.LOGIN_SESSION_KEY);
        getSession().removeAttribute(Const.LAST_REFERER);
        Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "index";
    }

    @RequestMapping(value="/uploadHeadPortrait")
    public String uploadHeadPortrait(){
        return "user/uploadheadportrait";
    }

    @RequestMapping(value="/newCategory")
    public String newCategory(){
        return "category/newcategory";
    }
}
