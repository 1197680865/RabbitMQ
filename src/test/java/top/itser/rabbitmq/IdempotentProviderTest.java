package top.itser.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.itser.rabbitmq.dto.OrderDto;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
public class IdempotentProviderTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * worker 模型 1分发到n个队列，无交换机
     */
    @Test
    public void WorkerOne2N() {
        //快捷键 10.fori
        for (int i = 0; i < 1; i++) {
            OrderDto dto = new OrderDto(null,"CX00"+i,"出行服务","U13691189699","憨憨");
            Message message = MessageBuilder.withBody(JSON.toJSONString(dto).getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setContentEncoding("utf-8")
                    .setMessageId(UUID.randomUUID().toString())
                    .build();
            rabbitTemplate.convertAndSend("work-idempotent", message);
        }
    }

}
