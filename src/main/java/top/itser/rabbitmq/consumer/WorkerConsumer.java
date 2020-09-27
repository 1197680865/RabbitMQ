package top.itser.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Worker- 消费者
 * 只有再创建消费者后，才会有队列
 * @Queue 默认是可持久化的、非独占、不自动删除
 * @RabbitListener 也可以定义在方法上，可以实现在一个类中产生多个消费者
 * 问题： 部分消息打印到了Tests中？
 */
@Component
//@RabbitListener(queuesToDeclare = @Queue(value = "hello"))
public class WorkerConsumer {

    /**
     * 消费者1
     * @param msg
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "worker"))
    public void reciver1(String msg){
        System.out.println("msg1" + msg);
    }

    /**
     * 消费者2
     * @param msg
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "worker"))
    public void reciver2(String msg){
        System.out.println("msg2" + msg);
    }

}
