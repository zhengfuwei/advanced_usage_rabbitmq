package com.cdxt.rabbitmqspringbootconsumer.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

/**
 * 消息消费者
 *
 * @author zhengfuwei
 * @date 2019年7月19日16:18:54
 */
@Component
@Slf4j
public class RabbitConsumer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 监听 queue-rabbit-springboot-advance 队列
     *
     * @param receiveMessage 接收到的消息
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "queue-rabbit-springboot-advance")
    public void receiveMessage(String receiveMessage, Message message, Channel channel) {
        try {
            // 手动签收
            log.info("接收到消息:[{}]", receiveMessage);
            if (new Random().nextInt(10) < 5) {
                log.warn("拒绝一条信息:[{}]，此消息将会由死信交换器进行路由.", receiveMessage);
                //deliveryTag:index multiple:is not batch process  requeue  :被拒绝的是否重新入队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } else {
                //deliveryTag:index multiple:is not batch process
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            log.info("接收到消息之后的处理发生异常.", e);
            try {
                //发送失败数据信息给第三方（最好采用熔断机制hystrix）
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e1) {
                log.error("签收异常.", e1);
            }
        }
        //日志保存(采用动态代理（最好用动态）-->日志系统)
    }
}
