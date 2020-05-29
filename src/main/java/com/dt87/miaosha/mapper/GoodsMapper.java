package com.dt87.miaosha.mapper;


import com.dt87.miaosha.entity.Goods;
import com.dt87.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsMapper {
    public List<GoodsVo> selectGoods(@Param("goodsId") Long goodsId);

    public int reduceCount(Long goodsId);

}
