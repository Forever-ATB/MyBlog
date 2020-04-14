package com.atb.myblog.dao;

import com.atb.myblog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Date 2020/4/4 15:12
 * @Created by : 叶宗海
 */

public interface UserRepository extends JpaRepository<User,Long> {
   User findByUsernameAndPassword(String username,String password);
}
