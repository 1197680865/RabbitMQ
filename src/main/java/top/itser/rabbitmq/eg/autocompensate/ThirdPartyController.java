package top.itser.rabbitmq.eg.autocompensate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.itser.rabbitmq.core.result.CommonResult;
import top.itser.rabbitmq.dto.OrderDto;

/**
 * 模拟第三方服务
 * 方向： 生产者-> 消费者 -> ThirdPartyController
 * 只有ThirdPartyController成功接收后，消息才算投递成功
 * @author Zhangchen
 */
@RestController
public class ThirdPartyController {
    private final static Logger log = LoggerFactory.getLogger(ThirdPartyController.class);

    /**
     * 模拟接口异常开关
     */
    private boolean createError = false;


    @PostMapping("/compensate/createOrder")
    @ApiOperation(value = "第三方HTTP服务-下订单",tags = "自动补偿")
    public CommonResult<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        //...创建订单
        if (createError){
            int i =10/0;
            return new CommonResult<>(444,"订单创建失败",orderDto);
        }else {
            orderDto.setOrderId("O1903");
            log.info("ThirdPartyController,订单创建成功,{}",orderDto);
            return new CommonResult<>(200,"订单创建成功",orderDto);
        }
    }

    @PostMapping("/compensate/errorSwitch")
    @ApiOperation(value = "第三方HTTP服务-模拟接口异常开关",tags = "自动补偿")
    public CommonResult<Boolean> errorSwitch(
            @ApiParam(name = "f",value = "开关",example = "true")
            @RequestParam(name = "f",defaultValue = "false")
                                            Boolean f){
        createError = !createError;
        return new CommonResult<>(200,"",createError);

    }

}
