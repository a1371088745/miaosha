package com.dt87.miaosha.service.impl;

import com.dt87.miaosha.entity.MiaoshaOrder;
import com.dt87.miaosha.entity.OrderInfo;
import com.dt87.miaosha.entity.User;
import com.dt87.miaosha.mapper.GoodsMapper;
import com.dt87.miaosha.mapper.OrderInfoMapper;
import com.dt87.miaosha.mapper.OrderMapper;
import com.dt87.miaosha.redis.MiaoshaKey;
import com.dt87.miaosha.redis.RedisService;
import com.dt87.miaosha.service.OrderService;
import com.dt87.miaosha.util.MD5Util;
import com.dt87.miaosha.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public String creatPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid());
        String key = user.getId() + "_" + goodsId;
        this.redisService.set(MiaoshaKey.getpath, key, str);
        return key;
    }

    @Override
    @Transactional
    public int insertOrder(OrderInfo orderInfo, Long goodsId) {
        int i = goodsMapper.reduceCount(goodsId);
        if (i <= 0) {
            setGoodsOver(goodsId);
            return 0;
        }
        Long c = orderInfoMapper.insertOrderInfo(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(orderInfo.getUserId());
        miaoshaOrder.setGoodsId(goodsId);
        miaoshaOrder.setOrderId(orderInfo.getId());
        return orderMapper.insertOrder(miaoshaOrder);
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        OrderInfo order = this.orderInfoMapper.selectOrderInfo(userId, goodsId);
        //三种情况
        if (order != null) {
            return order.getId();
        } else {
            Boolean isOver = getGoodsOver(goodsId);

            if (isOver) {  //秒杀失败
                return -1;
            } else {  //
                return 0; //继续轮询
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        this.redisService.set(MiaoshaKey.getIsGoodsOver, "" + goodsId, true);  //true 是失败

    }

    private Boolean getGoodsOver(Long goodsId) {
        return this.redisService.exists(MiaoshaKey.getIsGoodsOver, "" + goodsId);

    }

    public BufferedImage createVerifyCode(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 90;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, rnd);
        //输出图片
        return image;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);  // 1*5+2
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[]{'+', '-', '*'};

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        int num4 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        char op3 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3 + op3 + num4;
        return exp;
    }

    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if (codeOld == null || codeOld - verifyCode != 0) {
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId);
        return true;
    }

}
