package com.cr.campusmicroblog.service;

import com.cr.campusmicroblog.entity.User;

import java.util.Map;

/**
 * @author cr
 * @date 2020-11-16 23:40
 */
public interface UserService {
    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return 用户对象
     */
    User getUser(Integer userId);

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 注册结果
     */
    Map<String, Object> register(String username, String password);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    Map<String, Object> login(String username, String password);

    /**
     * 用户注销
     *
     * @param ticket ticket
     */
    void logout(String ticket);
}
