package top.itser.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * HelloWorld- 消费者
 * 只有再创建消费者后，才会有队列
 * @Queue 默认是可持久化的、非独占、不自动删除
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(value = "hello"))
//@RabbitListener(queuesToDeclare = @Queue(value = "hello",durable = "true",autoDelete = "false",exclusive = "false"))
public class HelloWorldConsumer {

    @RabbitHandler
    public void reciver1(String msg){
        System.out.println(msg);
    }
}
