package com.dt87.miaosha.rabbit;

import com.dt87.miaosha.entity.MiaoshaMessage;
import com.dt87.miaosha.entity.OrderInfo;
import com.dt87.miaosha.redis.OrderKey;
import com.dt87.miaosha.redis.RedisService;
import com.dt87.miaosha.service.GoodsService;
import com.dt87.miaosha.service.OrderInfoService;
import com.dt87.miaosha.service.OrderService;
import com.dt87.miaosha.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    OrderServiceImpl orderServiceImpl;


    //		@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
//		public void receive(String message) {
//			log.info("receive message:"+message);
//			MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
//			MiaoshaUser user = mm.getUser();
//			long goodsId = mm.getGoodsId();
//			
//			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//	    	int stock = goods.getStockCount();
//	    	if(stock <= 0) {
//	    		return;
//	    	}
//	    	//判断是否已经秒杀到了
//	    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//	    	if(order != null) {
//	    		return;
//	    	}
//	    	//减库存 下订单 写入秒杀订单
//	    	miaoshaService.miaosha(user, goods);
//		}
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(message, MiaoshaMessage.class);
        OrderInfo orderInfo = orderInfoService.selectOrderInfo(miaoshaMessage.getOrderInfo().getUserId(), miaoshaMessage.getGoodsId());
        if (null != orderInfo) {
            return;
        }
        orderServiceImpl.insertOrder(miaoshaMessage.getOrderInfo(), miaoshaMessage.getGoodsId());
        redisService.set(OrderKey.orderInfoKey, miaoshaMessage.getOrderInfo().getUserId() + "_" + miaoshaMessage.getGoodsId(), miaoshaMessage.getOrderInfo());

    }
	
		/*@RabbitListener(queues=MQConfig.QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			
		}*/

    //-------------------------------------------
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info(" topic  queue1 message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info(" topic  queue2 message:" + message);
    }

    //------------------------------------------------
    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message) {
        log.info(" header  queue message:" + new String(message));
    }


}
