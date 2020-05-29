package com.dt87.miaosha.service;

import com.dt87.miaosha.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    public List<GoodsVo> selectGoods(Long goodsId);

    public int reduceCount(Long goodsId);
}
