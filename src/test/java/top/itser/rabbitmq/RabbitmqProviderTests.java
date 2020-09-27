package top.itser.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
public class RabbitmqProviderTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * holleWorld、直连模式 1-1
     */
    @Test
    public void holleWorld_One2One() {
        rabbitTemplate.convertAndSend("hello", "来自holleWorld模式的消息");
    }

    /**
     * worker 模型 1分发到n个队列，无交换机
     */
    @Test
    public void WorkerOne2N() {
        //快捷键 10.fori
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("worker", "来自worker模式的消息" + i);
        }
    }

    /**
     * fanout 模型  广播模型
     */
    @Test
    public void fanout() {
        //快捷键 10.fori
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("logs", "", "来自fanout模式的消息" + i);
        }
    }


    /**
     * 路由模型  直连
     */
    @Test
    public void routingDirect() {
        rabbitTemplate.convertAndSend("logs-directs", "info", "来自routing-Direct模式的消息：info" );
        rabbitTemplate.convertAndSend("logs-directs", "error", "来自routing-Direct模式的消息：error" );
    }

    /**
     * topic订阅模式
     */
    @Test
    public void topic() {
        rabbitTemplate.convertAndSend("logs-topic", "order.save", "来自topic模式的消息：order.save" );
        rabbitTemplate.convertAndSend("logs-topic", "user.save", "来自topic模式的消息：user.save" );
        rabbitTemplate.convertAndSend("logs-topic", "order.match.cancel", "来自topic模式的消息：order.match.cancel" );
    }


}
