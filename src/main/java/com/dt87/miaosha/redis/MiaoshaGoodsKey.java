package com.dt87.miaosha.redis;

public class MiaoshaGoodsKey extends BasePrefix {

    private MiaoshaGoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaGoodsKey getGoodsListKey = new MiaoshaGoodsKey(60, "goodsList");
    public static MiaoshaGoodsKey getGoodsKey = new MiaoshaGoodsKey(60, "goods");
    public static MiaoshaGoodsKey getGoodsCountKey = new MiaoshaGoodsKey(0, "goodsCount");
}
