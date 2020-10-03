package top.itser.rabbitmq.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetryInfo {
    private String url;
    private Object msg;
    private Object lastErrorMsg;
}
