package com.dt87.miaosha.service.impl;

import com.dt87.miaosha.entity.OrderInfo;
import com.dt87.miaosha.mapper.OrderInfoMapper;
import com.dt87.miaosha.redis.OrderKey;
import com.dt87.miaosha.redis.RedisService;
import com.dt87.miaosha.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public Long insertOrderInfo(OrderInfo orderInfo) {
        return orderInfoMapper.insertOrderInfo(orderInfo);
    }

    //先在redis查询，如果redis查询不到则查询数据库
    @Override
    public OrderInfo selectOrderInfo(Long userId, Long goodsId) {
        OrderInfo orderInfo = redisService.get(OrderKey.orderInfoKey, userId + "_" + goodsId, OrderInfo.class);
        if (null != orderInfo) {
            return orderInfo;
        }
        OrderInfo orderInfo1 = orderInfoMapper.selectOrderInfo(userId, goodsId);
        return orderInfo1;
    }

    @Override
    public OrderInfo selectOrderInfoById(Long orderInfoId) {
        return orderInfoMapper.selectOrderInfoById(orderInfoId);
    }

    @Override
    public Boolean updateOrderState(String orderNum) {
        int i = orderInfoMapper.updateOrderState(orderNum);
        if (i == 0) {
            return false;
        }
        return true;
    }

}
