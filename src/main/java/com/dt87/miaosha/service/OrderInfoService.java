package com.dt87.miaosha.service;

import com.dt87.miaosha.entity.OrderInfo;

public interface OrderInfoService {
    public Long insertOrderInfo(OrderInfo orderInfo);

    public OrderInfo selectOrderInfo(Long userId, Long goodsId);

    public OrderInfo selectOrderInfoById(Long orderInfoId);

    public Boolean updateOrderState(String orderNum);
}
