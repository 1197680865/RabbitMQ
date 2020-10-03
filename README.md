# RabbitMQ
### RabbitMQ入门-实战
为知笔记 https://6362f384.wiz03.com/wapp/pages/view/share/s/1zoLe40CH44v2irh1C1qEcRA2Nv1qM2WBkGg2FzA0g0A4SCL

### RabbitMQ 自动补偿，失败重试
情况1: 消费者获取到消息后，抛出数据转换异常，是否需要重试? 不需要重试,因为属于程序bug需要重新发布版本

情况2: 消费者获取到消息后，调用第三方接口，但接口暂时无法访问，是否需要重试? 需要重试，可能是因为网络原因短暂不能访问

**见：top.itser.rabbitmq.eg.autocompensate.Consumer**

### 幂等性
**幂等性：**对同一个系统，使用同样的条件，一次请求和重复的多次请求对系统资源的影响是一致的。

幂等常用思路：

（1）乐观锁，Version

在数据更新时需要去比较持有数据的版本号，版本号不一致的操作无法成功。例如博客点赞次数自动+1的接口

（2）去重表

利用数据库表单的特性来实现幂等，常用的一个思路是在表上构建唯一性索引，保证某一类数据一旦执行完毕，后续同样的请求再也无法成功写入。

（3）Token，唯一标识

适用范围较广，有多种不同的实现方式。其核心思想是为每一次操作生成一个唯一性的凭证，也就是token。一个token在操作的每一个阶段只有一次执行权，一旦执行成功则保存执行结果。对重复的请求，返回同一个结果。

**见top.itser.rabbitmq.eg.idempotent.IdempotentConsumer**

### 消息确认
**见master top.itser.rabbitmq.eg.ack.AckDemoConsumer**