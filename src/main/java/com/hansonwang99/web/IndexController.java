package com.hansonwang99.web;

import com.hansonwang99.comm.Const;
import com.hansonwang99.domain.Category;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.CategoryService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value="显示网站首页视图", notes="显示网站首页视图")
    @RequestMapping(value="/",method= RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    @ApiOperation(value="显示用户登录视图", notes="显示用户登录视图")
    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @ApiOperation(value="显示主框架视图", notes="显示主框架视图")
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

    @ApiOperation(value="显示弹出式编辑器md书写区视图", notes="显示弹出式编辑器md书写区视图")
    @RequestMapping(value="/write",method= RequestMethod.GET)
    public String write(Model model) {
        return "write";
    }

    @ApiOperation(value="显示用户注册视图", notes="显示用户注册视图")
    @RequestMapping(value="/register",method= RequestMethod.GET)
    public String register(Model model) {
        return "register";
    }

    @ApiOperation(value="显示用户登出返回网站首页视图", notes="显示用户登出返回网站首页视图")
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

    @ApiOperation(value="显示上传用户图像视图", notes="显示上传用户图像视图")
    @RequestMapping(value="/uploadHeadPortrait",method = RequestMethod.POST)
    public String uploadHeadPortrait(){
        return "user/uploadheadportrait";
    }

    @ApiOperation(value="显示新建文章分类视图", notes="显示新建文章分类视图")
    @RequestMapping(value="/newCategory",method = RequestMethod.POST)
    public String newCategory(){
        return "category/newcategory";
    }

    @ApiOperation(value="显示身高百分位表视图", notes="显示身高百分位表视图")
    @RequestMapping(value="/heightsheet",method = RequestMethod.POST)
    public String getHeightSheet(Model model){
        return "tool/heightsheet";
    }
}
