package com.dt87.miaosha.service.impl;

import com.dt87.miaosha.entity.User;
import com.dt87.miaosha.mapper.UserMapper;
import com.dt87.miaosha.redis.MiaoshaUserKey;
import com.dt87.miaosha.redis.RedisService;
import com.dt87.miaosha.result.CodeMsg;
import com.dt87.miaosha.service.UserService;
import com.dt87.miaosha.util.MD5Util;
import com.dt87.miaosha.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {
    public static final String TOKEN_NAME = "token";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
    public int j = 1;

    @Override
    public CodeMsg login(Long id, String password, HttpServletResponse response) {
        User user = userMapper.findUser(id);
        if (user == null) {
            return CodeMsg.MOBILE_NULL;
        }
        String salt = user.getSalt();
        String s = MD5Util.formPassToDBPass(password, salt);
        if (!user.getPassword().equals(s)) {
            return CodeMsg.PASSWORD_ERROR;
        }
        String token = UUIDUtil.uuid();
        addCookieToken(response, token, user);
        return CodeMsg.SUCCESS;
    }

    public void addCookieToken(HttpServletResponse response, String token, User user) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setMaxAge(60000 * 60 * 24 * 2);
        cookie.setPath("/");
        response.addCookie(cookie);
        redisService.set(MiaoshaUserKey.token, token, user);

    }

    public User getUserByToken(String token, HttpServletResponse response) {
        User user = redisService.get(MiaoshaUserKey.token, token, User.class);
        if (user == null) {
            return null;
        }
        addCookieToken(response, token, user);
        return user;
    }
}
