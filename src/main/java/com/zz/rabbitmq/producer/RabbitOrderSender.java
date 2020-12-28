package com.zz.rabbitmq.producer;

import com.zz.rabbitmq.constant.Constants;
import com.zz.rabbitmq.entity.Order;
import com.zz.rabbitmq.mapper.BrokeMessageLogMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RabbitOrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokeMessageLogMapper brokeMessageLogMapper;

    //回调函数 confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String s) {
            System.out.println("correlationData: " + correlationData);
            String messageId = correlationData.getId();
            if (ack) {
                brokeMessageLogMapper.changeBrokeMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS, new Date());
            }else{
                System.out.println("====异常处理====");
            }
        }
    };


    //发送消息方法调用：构建自定义对象信息
    public void sendOrder(Order order){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //消息唯一Id
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange", "order.abc", order, correlationData);

    }


}
