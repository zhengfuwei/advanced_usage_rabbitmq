package com.cdxt.rabbitmqspringbootproducer.callback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 生产者(服务端)消息确认
 *
 * @author zhengfuwei
 * @date 2019年7月19日16:06:20
 */
@Slf4j
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("(start)生产者消息确认=========================");
        log.info("correlationData:[{}]", correlationData);
        log.info("ack:[{}]", ack);
        log.info("cause:[{}]", cause);
        if (!ack) {
            log.info("消息可能未到达rabbitmq服务器");
        }
        log.info("(end)生产者消息确认=========================");
    }
}
