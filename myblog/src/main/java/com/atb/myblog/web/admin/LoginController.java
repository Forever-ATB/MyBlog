package com.atb.myblog.web.admin;

import com.atb.myblog.po.User;
import com.atb.myblog.service.UserService;
import com.atb.myblog.util.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Date 2020/4/4 15:05
 * @Created by : 叶宗海
 * @Description : TODO
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String loginPage(){
        return "admin/login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){

        User user = userService.checkUser(username, Md5.getMD5(password));

        if(user != null){
            session.setAttribute("user",user);
            return "admin/index";
        }else{
            //Model重定向拿不到
            attributes.addFlashAttribute("message","用户名和密码错误");
            return "redirect:/admin/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin/";
    }
}
