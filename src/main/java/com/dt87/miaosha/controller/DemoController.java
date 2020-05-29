package com.dt87.miaosha.controller;

import com.dt87.miaosha.rabbit.MQSender;
import com.dt87.miaosha.redis.RedisService;
import com.dt87.miaosha.result.Result;
import com.dt87.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/rabbit")
@Controller
public class DemoController {
    @Autowired
    private UserService userService;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
        mqSender.send("hello,world");
        return Result.success("hello world");
    }


}
