package com.hansonwang99.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by hansonwang on 2017/6/2.
 */
@Controller
public class IndexController {

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
}
