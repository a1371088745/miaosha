package com.dt87.miaosha.vo;


import com.dt87.miaosha.entity.User;

public class GoodsDetailVo {

    private User user;
    private GoodsVo goods;

    // 秒杀状态
    private long miaoshaStatus;
    // 剩余时间
    private long remainSeconds;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public long getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(long miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }


}
