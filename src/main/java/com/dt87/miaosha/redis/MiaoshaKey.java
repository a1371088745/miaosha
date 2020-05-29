package com.dt87.miaosha.redis;

public class MiaoshaKey extends BasePrefix {

    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey getIsGoodsOver = new MiaoshaKey(0, "isGoodsOver");

    public static MiaoshaKey getpath = new MiaoshaKey(50, "path");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(60, "miaoshaVerifyCode");
}
