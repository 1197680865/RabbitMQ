package top.itser.rabbitmq.eg.idempotent;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.itser.rabbitmq.core.result.CommonResult;
import top.itser.rabbitmq.dto.OrderDto;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 实现保证幂等性的消费者
 * @author Zhangchen
 */
@Component
public class IdempotentConsumer {
    private final static Logger log = LoggerFactory.getLogger(IdempotentConsumer.class);
    private final static String URL_CREATE_ORDER = "http://127.0.0.1:9000/compensate/createOrder";
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queuesToDeclare = @Queue(value = "work-idempotent"))
    public void consumerOne(Message message) throws Exception {
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("consumerOne收到消息，MessageID：{}，Msg：{}",messageId,msg);
        //检查全局 messageId 是否被消费过。
        String gValue = redisTemplate.opsForValue().get("msgid_"+messageId)+"";
        //gValue!="1"，说明未被消费；gValue=="1"，说明已被消费
        if ("1".equals(gValue)){
            log.warn("消息已被消费，幂等保证。{}",msg);
            return; // 已被消费了
        }
        OrderDto orderDto = JSON.parseObject(msg, OrderDto.class);
        ResponseEntity<CommonResult> responseEntity = restTemplate.postForEntity(URL_CREATE_ORDER, orderDto, CommonResult.class);
        if ( responseEntity.getBody().getCode()!=200)
        {
            log.error("consumerOne,消费异常,{}",msg);
            log.error("consumerOne,消费异常,接收信息,{}",responseEntity.getBody().toString());
            //手动抛异常，触发重试机制
            throw new Exception("消费异常");
        }else {
            log.info("consumerOne,消费成功,{}",msg);
            //修改全局的 messageId状态为1。 时效为24小时
            redisTemplate.opsForValue().set("msgid_"+messageId,"1",24, TimeUnit.HOURS);
        }
    }
}
