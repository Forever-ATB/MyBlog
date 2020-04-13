package com.atb.myblog.service;

import com.atb.myblog.po.User;

/**
 * @Date 2020/4/4 15:08
 * @Created by : 叶宗海
 * @Description : TODO
 */

public interface UserService {

    public User checkUser(String username,String password);
}
