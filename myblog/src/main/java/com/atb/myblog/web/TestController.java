package com.atb.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Date 2020/4/5 16:16
 * @Created by : 叶宗海
 * @Description : TODO
 */

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/")
    public String name(Model model){
        model.addAttribute("image","../images/avatar.png");
        return "test";
    }
}
