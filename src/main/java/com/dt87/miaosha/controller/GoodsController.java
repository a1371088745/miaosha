package com.dt87.miaosha.controller;

import com.dt87.miaosha.entity.MiaoshaGoods;
import com.dt87.miaosha.entity.User;
import com.dt87.miaosha.redis.MiaoshaGoodsKey;
import com.dt87.miaosha.redis.RedisService;
import com.dt87.miaosha.result.Result;
import com.dt87.miaosha.service.impl.GoodsServiceImpl;
import com.dt87.miaosha.vo.GoodsDetailVo;
import com.dt87.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController implements InitializingBean {
    @Autowired
    private GoodsServiceImpl goodsService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private RedisService redisService;

    public static Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    @RequestMapping(value = "/toGoodsList", produces = "text/html")
    @ResponseBody
    public String toGoodsList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {
        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        String html = redisService.get(MiaoshaGoodsKey.getGoodsListKey, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            System.out.println("从redis里面进入的");
            return html;
        }
        List<GoodsVo> goodsList = goodsService.selectGoods(null);
        model.addAttribute("goodsList", goodsList);
        WebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            this.redisService.set(MiaoshaGoodsKey.getGoodsListKey, "", html);
        }
        System.out.println("没从redis进入");
        return html;

    }

    //redis缓存订单详情页面
    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail(@PathVariable("goodsId") Long goodsId, Model model, User user, HttpServletRequest request, HttpServletResponse response) {
        if (user == null) {
            return "login";
        }
        String html = redisService.get(MiaoshaGoodsKey.getGoodsKey, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            System.out.println("从redis获取页面");
            return html;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        Date nowDate = new Date();
        long time = nowDate.getTime();
        List<GoodsVo> goodsList = goodsService.selectGoods(goodsId);
        map.put("goods", goodsList.get(0));
        System.out.println(goodsList.get(0).getStartDate());
        long startTime = goodsList.get(0).getStartDate().getTime();
        long endTime = goodsList.get(0).getEndDate().getTime();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (time < startTime) {
            miaoshaStatus = 0;
            remainSeconds = (int) ((startTime - time) / 1000);
        } else if (time < endTime) {
            miaoshaStatus = 1;
            remainSeconds = 0;
        } else {
            miaoshaStatus = 2;
            remainSeconds = -1;
        }
        map.put("miaoshaStatus", miaoshaStatus);
        map.put("remainSeconds", remainSeconds);
        model.addAllAttributes(map);
        WebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(MiaoshaGoodsKey.getGoodsKey, "" + goodsId, html);
            System.out.println("不是从redis获取页面");
            return html;
        }
        return html;
    }

    //网页静态分离
    @RequestMapping(value = "/to_detail1/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(@PathVariable("goodsId") Long goodsId, User user) {
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setUser(user);
        Date nowDate = new Date();
        long time = nowDate.getTime();
        List<GoodsVo> goodsList = goodsService.selectGoods(goodsId);
        goodsDetailVo.setGoods(goodsList.get(0));
        long startTime = goodsList.get(0).getStartDate().getTime();
        long endTime = goodsList.get(0).getEndDate().getTime();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (time < startTime) {
            miaoshaStatus = 0;
            remainSeconds = (int) ((startTime - time) / 1000);
        } else if (time < endTime) {
            miaoshaStatus = 1;
            remainSeconds = 0;
        } else {
            miaoshaStatus = 2;
            remainSeconds = -1;
        }
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVo);
    }


    //初始化redis库存
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.selectGoods(null);
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(MiaoshaGoodsKey.getGoodsCountKey, "" + goodsVo.getGoodsId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getGoodsId(), false); //false为还有库存
        }

    }
}
