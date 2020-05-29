package com.dt87.miaosha.service.impl;

import com.dt87.miaosha.mapper.GoodsMapper;
import com.dt87.miaosha.service.GoodsService;
import com.dt87.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> selectGoods(Long goodsId) {
        return goodsMapper.selectGoods(goodsId);
    }

    @Override
    public int reduceCount(Long goodsId) {
        return goodsMapper.reduceCount(goodsId);
    }

    public String getOrderIdByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }
}
