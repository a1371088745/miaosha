package com.dt87.miaosha.controller;

import com.dt87.miaosha.entity.*;
import com.dt87.miaosha.mapper.GoodsMapper;
import com.dt87.miaosha.mapper.OrderInfoMapper;
import com.dt87.miaosha.rabbit.MQSender;
import com.dt87.miaosha.redis.AccessKey;
import com.dt87.miaosha.redis.MiaoshaGoodsKey;
import com.dt87.miaosha.redis.MiaoshaKey;
import com.dt87.miaosha.redis.RedisService;
import com.dt87.miaosha.result.CodeMsg;
import com.dt87.miaosha.result.Result;
import com.dt87.miaosha.service.OrderService;
import com.dt87.miaosha.service.impl.GoodsServiceImpl;
import com.dt87.miaosha.service.impl.OrderInfoServiceImpl;
import com.dt87.miaosha.service.impl.OrderServiceImpl;
import com.dt87.miaosha.vo.GoodsVo;
import com.dt87.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender sender;
    @Autowired
    private GoodsServiceImpl goodsService;
    @Autowired
    private OrderInfoServiceImpl orderInfoService;

    @RequestMapping("/insertOrder")
    public String insertOrder(Long goodsId, Model model, User user) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        List<GoodsVo> goodsVosList = goodsMapper.selectGoods(goodsId);
        GoodsVo goodsVo = goodsVosList.get(0);
        if (goodsVo.getStockCount() <= 0) {
            model.addAttribute("msg", "没有库存了");
            return "miaosha_error";
        }
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfo(user.getId(), goodsId);
        if (orderInfo != null) {
            model.addAttribute("msg", "您已参与过该商品的秒杀");
            return "miaosha_error";
        }
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setCreateDate(new Date());
        orderInfo1.setDeliveryAddrId(0L);
        orderInfo1.setGoodsCount(1);
        orderInfo1.setGoodsId(goodsVo.getId());
        orderInfo1.setGoodsName(goodsVo.getGoodsName());
        orderInfo1.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo1.setGoodsChannel(1);
        orderInfo1.setStatus(0);
        orderInfo1.setUserId(user.getId());
        orderService.insertOrder(orderInfo1, goodsId);
        model.addAttribute("orderInfo", orderInfo1);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }

    //ajax渲染数据
    @RequestMapping("/insertOrder1")
    @ResponseBody
    public Result<OrderDetailVo> insertOrder1(Long goodsId, User user) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        List<GoodsVo> goodsVosList = goodsMapper.selectGoods(goodsId);
        GoodsVo goodsVo = goodsVosList.get(0);
        if (goodsVo.getStockCount() <= 0) {
            return Result.error(CodeMsg.NO_COUNT);
        }
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfo(user.getId(), goodsId);
        if (orderInfo != null) {
            return Result.error(CodeMsg.AGAIN);
        }
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setCreateDate(new Date());
        orderInfo1.setDeliveryAddrId(0L);
        orderInfo1.setGoodsCount(1);
        orderInfo1.setGoodsId(goodsVo.getId());
        orderInfo1.setGoodsName(goodsVo.getGoodsName());
        orderInfo1.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo1.setGoodsChannel(1);
        orderInfo1.setStatus(0);
        orderInfo1.setUserId(user.getId());
        orderService.insertOrder(orderInfo1, goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoods(goodsVosList.get(0));
        orderDetailVo.setOrder(orderInfo1);
        return Result.success(orderDetailVo);
    }

    //使用redis加消息队列完成秒杀
    @RequestMapping("/{path}/insertOrder2")
    @ResponseBody
    public Result<MiaoshaMessage> insertOrder2(Long goodsId, User user, @PathVariable String path) {

        String redisPath = redisService.get(MiaoshaKey.getpath, path, String.class);
        if (redisPath == null) {
            return Result.error(CodeMsg.ERROR_PATh);
        }
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        if (GoodsController.localOverMap.get(goodsId)) {
            return Result.error(CodeMsg.NO_COUNT);
        }
        Long decr = redisService.decr(MiaoshaGoodsKey.getGoodsCountKey, "" + goodsId);
        if (decr < 0) {
            //无库存了
            GoodsController.localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.NO_COUNT);
        }
        OrderInfo orderInfo = orderInfoService.selectOrderInfo(user.getId(), goodsId);
        if (null != orderInfo) {
            return Result.error(CodeMsg.AGAIN);
        }
        GoodsVo goodsVo = goodsMapper.selectGoods(goodsId).get(0);
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setCreateDate(new Date());
        orderInfo1.setDeliveryAddrId(0L);
        orderInfo1.setGoodsCount(1);
        orderInfo1.setGoodsId(goodsVo.getId());
        orderInfo1.setGoodsName(goodsVo.getGoodsName());
        orderInfo1.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo1.setGoodsChannel(1);
        orderInfo1.setStatus(0);
        orderInfo1.setUserId(user.getId());
        String orderIdByTime = goodsService.getOrderIdByTime();
        orderInfo1.setOrderNumber(orderIdByTime);
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setOrderInfo(orderInfo1);
        sender.sendMiaoshaMessage(miaoshaMessage);
        return Result.success(miaoshaMessage);
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(User user, Model model, @RequestParam(value = "goodsId") long goodsId) {
        //如果没有登录跳转到登录页面
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        model.addAttribute("user", user);
        long result = this.orderService.getMiaoshaResult(user.getId(), goodsId);

        return Result.success(result);
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> detail(User user, Long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        OrderInfo orderInfo = orderInfoService.selectOrderInfoById(orderId);
        List<GoodsVo> goodsVos = goodsMapper.selectGoods(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(orderInfo);
        orderDetailVo.setGoods(goodsVos.get(0));
        return Result.success(orderDetailVo);
    }

    @RequestMapping("/path")
    @ResponseBody
    public Result<String> getPath(User user, Long goodsId, @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode, HttpServletRequest request) {
        if (null == user) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        String uri = request.getRequestURI();
        String key = uri + "_" + user.getId() + "_" + goodsId;
        Integer count = redisService.get(AccessKey.getAccessKey, key, Integer.class);
        if (count == null) {
            redisService.set(AccessKey.getAccessKey, key, 1);
        } else if (count < 5) {
            redisService.incr(AccessKey.getAccessKey, key);
        } else {
            return Result.error(CodeMsg.REQUEST_QUICK);
        }
        boolean check = orderService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = orderService.creatPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, User user,
                                              @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        try {
            BufferedImage image = orderService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

}
