package com.dt87.miaosha.controller;

import com.dt87.miaosha.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/alipay")
public class AlipayController {
    @Autowired
    private OrderInfoService orderInfoService;

    @RequestMapping("/updateOrderState")
    @ResponseBody
    public String updateOrderState(HttpServletRequest request) {
        try {
            String orderNum = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            Boolean flag = orderInfoService.updateOrderState(orderNum);
            if (flag) {
                return "success";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
