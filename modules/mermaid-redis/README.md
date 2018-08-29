# mermaid-redis
## 1.Introduction
**mermaid-redis**是redis访问组件，支持Redis的缓存、分布式锁、分布式计数器队列以及Topic（pub/sub队列）
## 2.QuickStart
```xml
<dependency>
    <groupId>com.mermaid.framework</groupId>
    <artifactId>mermaid-redis</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
## 3.mermaid-redis配置清单
```text
#Redis服务器
spring.redis.host=192.168.56.1
#Redis服务器连接端口
spring.redis.port=6379

spring.redis.password=
#redis数据索引
spring.redis.database=0
spring.redis.pool.max-active=8
#redis连接池达到最大连接数后，新的redis连接获取请求等待时间，单位：毫秒，“-1”表示无限等待
spring.redis.pool.max-wait=-1
spring.redis.pool.max-idle=8
spring.redis.timeout=0
```