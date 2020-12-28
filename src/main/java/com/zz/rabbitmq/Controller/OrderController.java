package com.zz.rabbitmq.Controller;

import com.zz.rabbitmq.entity.Order;
import com.zz.rabbitmq.producer.OrderSender;
import com.zz.rabbitmq.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/rabbitmq")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSender orderSender;

    @RequestMapping("/create")
    public void create(@RequestParam("id") String id){
        System.out.println("=====create======");
        Order order = new Order();
        order.setId(id);
        order.setMessageId(UUID.randomUUID().toString());
        order.setName("test 投递确认");
        orderService.createOrder(order);
    }

    @RequestMapping("/test")
    public void test(){
        System.out.println("=====test======");
        orderService.test();
    }

    @RequestMapping("/testOrder")
    public void testOrder(){
        System.out.println("=====testOrder======");
        Order order = new Order();
        order.setId(String.valueOf(System.currentTimeMillis()));
        order.setMessageId(UUID.randomUUID().toString());
        order.setName("第一次测试");
        orderSender.sendOrder(order);
    }

}
