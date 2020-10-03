package top.itser.rabbitmq.eg.ack;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import io.lettuce.core.dynamic.annotation.Key;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.itser.rabbitmq.dto.OrderDto;
import top.itser.rabbitmq.pojo.RetryInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Zhangchen
 */
@Component
@Slf4j
public class AckDemoConsumer {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private final static String RETRY_ORDER_CREAT ="retry_order_create";

    @RabbitListener(bindings =
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "ex-order",type = "topic"),
                    key = {"order.create"}
            )
    )
    public void consumerOne(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        try{
            int i=10/0;
            OrderDto orderDto = JSON.parseObject(msg, OrderDto.class);
            //basicAck参数说明
            //deliveryTag（唯一标识 ID）：当一个消费者向 RabbitMQ 注册后，会建立起一个 Channel ，RabbitMQ 会用 basic.deliver 方法向消费者推送消息，这个方法携带了一个 delivery tag， 它代表了 RabbitMQ 向该 Channel 投递的这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel
            //multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
           channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            log.info("consumerOne消费成功,{}",orderDto);
        }catch (Exception e)
        {
            //一般设置不重回队列，手动补偿，如放到db然后定时任务检查
            log.error("consumerOne消费失败，{},{}",msg,e);
            //basicNack 参数说明
            //deliveryTag（唯一标识 ID）：上面已经解释了。
            //multiple：开启批确认
            //requeue： true ：重回队列，false ：丢弃.  一般选择丢弃，否则会进行无限重试
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            //补偿手段
            RetryInfo retryInfo = new RetryInfo(null,msg,e.getMessage());
            redisTemplate.opsForList().rightPush(RETRY_ORDER_CREAT, JSON.toJSONString(retryInfo));

            //basicReject 参数说明
            //deliveryTag（唯一标识 ID）
            //requeue： true ：重回队列，false ：丢弃，在reject方法里必须设置true。
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);

            //basicNack 与 basicReject的区别
            // basicReject一次只能拒绝接收一个消息，
            // 而basicNack方法可以支持一次0个或多个消息的拒收，并且也可以设置是否requeue。
        }
    }
}
