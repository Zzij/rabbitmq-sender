package com.zz.rabbitmq.Task;

import com.google.gson.Gson;
import com.zz.rabbitmq.constant.Constants;
import com.zz.rabbitmq.entity.BrokeMessageLog;
import com.zz.rabbitmq.entity.Order;
import com.zz.rabbitmq.mapper.BrokeMessageLogMapper;
import com.zz.rabbitmq.producer.RabbitOrderSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Task {

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokeMessageLogMapper brokeMessageLogMapper;

    @Autowired
    private Gson gson;

    @Scheduled(initialDelay = 3000, fixedDelay = 60000)
    public void reSend(){
        System.out.println("=====定时任务启动=====");
        List<BrokeMessageLog> brokeList = brokeMessageLogMapper.query4StatusAndTimeOutMessage();
        brokeList.forEach(brokeMessageLog -> {
            if(brokeMessageLog.getTryCount() >= 3){
                brokeMessageLogMapper.changeBrokeMessageLogStatus(brokeMessageLog.getMessageId(), Constants.ORDER_SEND_FAILED, new Date());
            }else{
                //resend
                brokeMessageLogMapper.update4ReSend(brokeMessageLog.getMessageId(), new Date());
                Order order = gson.fromJson(brokeMessageLog.getMessage(), Order.class);
                try {
                    rabbitOrderSender.sendOrder(order);
                }catch (Exception e){
                    System.out.println("======异常======");
                }

            }
        });
    }
}
