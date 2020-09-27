package top.itser.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * FRoutingDirect 消费者
 * 只有再创建消费者后，才会有队列
 * @Queue 默认是可持久化的、非独占、不自动删除
 * @RabbitListener 也可以定义在方法上，可以实现在一个类中产生多个消费者
 */
@Component
//@RabbitListener(queuesToDeclare = @Queue(value = "hello"))
public class RoutingDirectConsumer {

    /**
     * 消费者1
     * @param msg
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,//不指定时，是临时队列
                    exchange = @Exchange(value = "logs-directs",type = "direct"), //指定交换机  direct为固定值不可变
                    key = {"info","warn","error"}
            )
    })
    public void reciver1(String msg){
        System.out.println("msg1 " + msg);
    }

    /**
     * 消费者2
     * @param msg
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,//不指定时，是临时队列
                    exchange = @Exchange(value = "logs-directs",type = "direct"), //指定交换机  direct为固定值不可变
                    key = {"error"}
            )
    })
    public void reciver2(String msg){
        System.out.println("msg2 " + msg);
    }




}
