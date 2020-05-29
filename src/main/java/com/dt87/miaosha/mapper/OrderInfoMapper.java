package com.dt87.miaosha.mapper;

import com.dt87.miaosha.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoMapper {
    public Long insertOrderInfo(@Param("orderInfo") OrderInfo orderInfo);

    public OrderInfo selectOrderInfo(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    public OrderInfo selectOrderInfoById(@Param("orderInfoId") Long orderInfoId);

    public int updateOrderState(@Param("orderNum") String orderNum);
}
