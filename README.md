# RabbitMQ
### RabbitMQ入门-实战
为知笔记 https://6362f384.wiz03.com/wapp/pages/view/share/s/1zoLe40CH44v2irh1C1qEcRA2Nv1qM2WBkGg2FzA0g0A4SCL

### RabbitMQ 自动补偿，失败重试
情况1: 消费者获取到消息后，抛出数据转换异常，是否需要重试? 不需要重试,因为属于程序bug需要重新发布版本

情况2: 消费者获取到消息后，调用第三方接口，但接口暂时无法访问，是否需要重试? 需要重试，可能是因为网络原因短暂不能访问

**见：top.itser.rabbitmq.eg.autocompensate.Consumer**
