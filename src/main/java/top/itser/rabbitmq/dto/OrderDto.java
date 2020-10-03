package top.itser.rabbitmq.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单dto
 * @author Zhangchen
 */
@Data
@ApiModel(value = "订单DTO",description = "订单对象")
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @ApiModelProperty(value = "订单号",example = "WXP20201002184800QA")
    private String orderId;
    @ApiModelProperty(value = "商品ID",example = "CX001")
    private String goodsId;
    @ApiModelProperty(value = "商品名称",example = "出行服务")
    private String goodsName;
    @ApiModelProperty(value = "顾客ID",example = "U13691189699")
    private String customerId;
    @ApiModelProperty(value = "顾客姓名",example = "憨憨")
    private String customerName;

}
