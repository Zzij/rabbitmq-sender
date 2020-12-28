package com.zz.rabbitmq.service;


import com.google.gson.Gson;
import com.zz.rabbitmq.constant.Constants;
import com.zz.rabbitmq.entity.BrokeMessageLog;
import com.zz.rabbitmq.entity.Order;
import com.zz.rabbitmq.mapper.BrokeMessageLogMapper;
import com.zz.rabbitmq.mapper.OrderMapper;
import com.zz.rabbitmq.producer.RabbitOrderSender;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokeMessageLogMapper brokeMessageLogMapper;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private Gson gson;

    public void createOrder(Order order){
        Date orderTime = new Date();
        System.out.println("111");
        orderMapper.insert(order);
        System.out.println("2222");
        BrokeMessageLog brokeMessageLog = new BrokeMessageLog();
        brokeMessageLog.setMessageId(order.getMessageId());
        brokeMessageLog.setMessage(gson.toJson(order));
        brokeMessageLog.setStatus(0);
        brokeMessageLog.setTryCount(0);
        brokeMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        brokeMessageLog.setCreateTime(new Date());
        brokeMessageLog.setUpdateTime(new Date());
        brokeMessageLogMapper.insert(brokeMessageLog);
        System.out.println("3333");
        try{
            rabbitOrderSender.sendOrder(order);
        }catch (Exception e){
            System.out.println("异常======");
        }
    }

    @Transactional
    public void testTransaction(){
        BrokeMessageLog brokeMessageLog = brokeMessageLogMapper.selectList().get(0);
        System.out.println(brokeMessageLog.getMessageId());
        try{
            System.out.println(new Date().toString());
            Thread.sleep(4000);
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println(new Date().toString());
        brokeMessageLog.setStatus(2);
        brokeMessageLogMapper.updateByPrimaryKeySelective(brokeMessageLog);
    }

    public void test(){
        /*for (int i = 0; i < 1; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    testTransaction();
                }
            });
            thread.start();
        }*/
        testTransaction();

    }
}
