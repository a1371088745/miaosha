package com.dt87.miaosha.controller;

import com.dt87.miaosha.entity.User;
import com.dt87.miaosha.result.CodeMsg;
import com.dt87.miaosha.result.Result;
import com.dt87.miaosha.service.impl.UserServiceImpl;
import com.dt87.miaosha.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result<CodeMsg> findUser(Long mobile, String password, HttpServletResponse response, User user) {
        if (user != null) {
            return Result.success(CodeMsg.SUCCESS);
        }
        if (password == null) {
            return Result.error(CodeMsg.PASSWORD_NULL);
        }
        if (!ValidatorUtil.isMobile("" + mobile)) {
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        CodeMsg login = userService.login(mobile, password, response);
        if (login.getCode() == 0) {
            return Result.success(null);
        } else {
            return Result.error(login);
        }
    }
}
