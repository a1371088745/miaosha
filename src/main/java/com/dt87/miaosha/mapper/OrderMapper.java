package com.dt87.miaosha.mapper;

import com.dt87.miaosha.entity.MiaoshaOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {
    public int insertOrder(@Param("miaoshaOrder") MiaoshaOrder miaoshaOrder);
}
