package com.hansonwang99.web;

import com.hansonwang99.comm.Const;
import com.hansonwang99.domain.Category;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hansonwang on 2017/6/2.
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Resource
    private CategoryService categoryService;

    @RequestMapping(value="/",method= RequestMethod.GET)
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

        model.addAttribute("user",getUser());
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

    @RequestMapping(value="/heightsheet")
    public String getHeightSheet(Model model){
        return "tool/heightsheet";
    }
}
