package com.dt87.miaosha.service;

import com.dt87.miaosha.entity.OrderInfo;
import com.dt87.miaosha.entity.User;

public interface OrderService {
    public String creatPath(User user, Long goodsId);

    public int insertOrder(OrderInfo orderInfo, Long goodsId);
}
