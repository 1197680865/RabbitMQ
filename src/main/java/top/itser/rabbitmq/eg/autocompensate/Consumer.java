package top.itser.rabbitmq.eg.autocompensate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.itser.rabbitmq.core.result.CommonResult;
import top.itser.rabbitmq.dto.OrderDto;
import top.itser.rabbitmq.pojo.RetryInfo;

/**
 * 演示自动补偿机制的消费者
 * 生产者在Test中 {@link //top.itser.rabbitmq.AutoCompensateProviderTest#WorkerOne2N()}
 * 异常开关：{@link ThirdPartyController#errorSwitch(java.lang.Boolean)}
 * yml文件中配置错误重试：    listener.simple.retry:
 * @author Zhangchen
 */
@Component
public class Consumer {
    private final static Logger log = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private final static String RETRY_ORDER_CREAT ="retry_order_create";
    private final static String URL_CREATE_ORDER = "http://127.0.0.1:9000/compensate/createOrder";

    @RabbitListener(queuesToDeclare = @Queue(value = "auto-compensate-worker"))
    public void consumerOne(String msg) throws Exception {

        ResponseEntity<CommonResult> responseEntity = restTemplate.postForEntity(URL_CREATE_ORDER, JSONObject.parseObject(msg,OrderDto.class), CommonResult.class);
        //如果第三方接口直接报错，原始状态码！=200，不会进入下面的方法，在postForEntity时抛出异常
        //如果第三方接口直接报错，原始状态码=200，但CommonResult中的code！=200,会进入下面的if = true，进行自定义重试：比如手动抛异常或者保存DB
        if ( responseEntity.getBody().getCode()!=200)
        {
            log.error("consumerOne,消费异常,{}",msg);
            log.error("consumerOne,消费异常,接收信息,{}",responseEntity.getBody().toString());
            //情况1: 消费者获取到消息后，调用第三方接口，但接口暂时无法访问，是否需要重试? （需要重试机制）
            //情况2: 消费者获取到消息后，抛出数据转换异常，是否需要重试?（一般不需要重试机制）。

            //重试方式一：手动抛出异常，然后触发重试
            //使用场景：接口暂时无法访问等
            //throw new Exception("消费异常");

            //重试方式二：记录到DB日志，定时任务（同时判断是否满足重发的时间限制）
            //使用场景：数据转换等异常
            //此时，应是确认成功消费了，不能抛异常
            RetryInfo retryInfo = new RetryInfo(URL_CREATE_ORDER,msg,responseEntity.getBody().toString());
            redisTemplate.opsForList().rightPush(RETRY_ORDER_CREAT, JSON.toJSONString(retryInfo));
        }else {
            log.info("consumerOne,消费成功,{}",msg);
        }
    }
//    @RabbitListener(queuesToDeclare = @Queue(value = "auto-compensate-worker"))
//    public void consumerTwo(String msg){
//        System.out.println(msg);
//    }
}
