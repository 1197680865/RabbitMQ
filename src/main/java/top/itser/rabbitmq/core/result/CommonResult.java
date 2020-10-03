package top.itser.rabbitmq.core.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  通用返回类型
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// 泛型 T，传入payment就返回payment
@ApiModel
public class CommonResult<T> {
    @ApiModelProperty(value = "状态码，200为成功")
    private Integer code;
    @ApiModelProperty(value = "信息")
    private String message;
    @ApiModelProperty(value = "返回实体")
    private T data;

    public CommonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
