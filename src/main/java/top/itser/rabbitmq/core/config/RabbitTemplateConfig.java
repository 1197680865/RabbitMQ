package top.itser.rabbitmq.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.itser.rabbitmq.eg.autocompensate.Consumer;

import javax.annotation.PostConstruct;

/**
 * @author Zhangchen
 */
@Component
public class RabbitTemplateConfig implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {
    private final static Logger log = LoggerFactory.getLogger(RabbitTemplateConfig.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate(){
        rabbitTemplate.setConfirmCallback(this);//监听消息是否到达 Broker 服务器
        rabbitTemplate.setReturnCallback(this);//监听消息是否通过交换器路由到队列
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b) {
           log.info("消息到达rabbitmq服务器");
        } else {
            log.error("消息可能未到达rabbitmq服务器");
        }

    }


    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.error("消息主体 message : " + message);
        log.error("消息主体 replyCode : " + i);
        log.error("描述 replyText：" + s);
        log.error("消息使用的交换器 exchange : " + s1);
        log.error("消息使用的路由键 routing : " + s2);

    }


}
