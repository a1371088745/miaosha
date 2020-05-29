package com.dt87.miaosha.service;

import com.dt87.miaosha.result.CodeMsg;

import javax.servlet.http.HttpServletResponse;


public interface UserService {
    public CodeMsg login(Long id, String password, HttpServletResponse response);
}
