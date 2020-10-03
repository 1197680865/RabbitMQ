package top.itser.rabbitmq.eg.ack;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.itser.rabbitmq.core.result.CommonResult;
import top.itser.rabbitmq.dto.OrderDto;

import java.util.UUID;

@RestController
public class AckController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/ack/send")
    @ApiOperation(value = "发送Topic消息",tags = "消息确认")
    public CommonResult<Boolean> sendMsg(
            @RequestParam(name = "sendCount") Integer sendCount){
        if (sendCount<=0) {
            return new CommonResult<>(400,"sendCount需大于零");
        }
        for (int i = 0; i < sendCount; i++) {
            OrderDto dto = new OrderDto(null,"TOPIC_CX00"+i,"出行服务","U13691189699","憨憨");
            Message message = MessageBuilder.withBody(JSON.toJSONString(dto).getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setContentEncoding("utf-8")
                    .setMessageId(UUID.randomUUID().toString())
                    .build();
            rabbitTemplate.convertAndSend("ex-order","order.create", message);
        }
        return new CommonResult<>(200,"");
    }
}
